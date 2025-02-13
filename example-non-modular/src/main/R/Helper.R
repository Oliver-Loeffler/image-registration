#' Creates a sub-grid for the given anchor point x
#' @description
#' Uses the given y_range vector as y-coordinate
#' and assigns the given x-coordinate to it.
#
#' @param x anchor point (x location)
#' @param y_range vector describing the y-grid
#' @return data.frame with refx and refy column with number
#'         of rows matching length of y_range.
generate_sub_grid <- function(x, y_range) {
    data.frame(
        "refx"=rep(x,length(y_range)),
        "refy"=y_range
    )
}

#' Adds noise (normal distribution) to the given sample (usually a vector).
#' @param vector
#' @param noise standard deviation (sd) for the rnorm function
#' @return vector with additional noise (only when noise > 0).
add_noise <- function(sample, noise=1) {
    if (noise>0) {
        return(sample+rnorm(1:length(sample), sd=noise))
    }
    return(sample)
}

#' Generates a regular grid of coordinates with noise to the displaced positions.
#' @param l lower bound for the grid (number of support points)
#' @param u upper bound for the grid
#' @param pitch pitch
#' @param noise standard deviation (sd) for the rnorm function
#' @return data.frame with refx, refy, posx, posy and diffx, diffy columns, plus a type column
generate_grid <- function(l=-2,u=2,pitch=1000, noise=0.01) {
    x_rng=(l:u)*pitch
    y_rng=(l:u)*pitch
    result <- do.call(rbind,lapply(x_rng, FUN=generate_sub_grid, y_rng))

    posx <- add_noise(result$refx, noise)
    posy <- add_noise(result$refy, noise)

    cbind(result,
         "posx"=posx,
         "posy"=posy,
         "diffx"=posx - result$refx,
         "diffy"=posy - result$refy,
         "type"="REG_MARK")
}

#' Generates a grid plot for the given data.frame.
#' @param R data.frame with refx, refy, posx, posy
#'          and diffx, diffy columns, plus a type column
#' @param margin Plot margin (percentage of )
#' @param main_title Plot title
plot_distortion_grid <- function(R, margin=0.1, main_title="Displacement Grid Plot") {
    # R must have:
    # x,y = Design Coordinates
    # posx, posy = Actual Coordinates
    # dx, dy = Displacement, e.g. dx = posx - x
    mg <- max(abs(range(R$refx)))*margin
    x_extent <- c(min(R$refx)-mg, max(R$refx)+mg)

    mg <- max(abs(range(R$refy)))*margin
    y_extent <- c(min(R$refy)-mg, max(R$refy)+mg)

    # Connect all points in column
    plot(c(R$posy,R$refy) ~ c(R$posx,R$refx),
         xlab = "X position",
         ylab = "Y position",
         main = main_title, col="lightgray", 
         pch=3, lwd=0.1, cex=0.1, xlim=x_extent, ylim=y_extent)

    rows <- unique(R$refx)
    for (r in rows) {
        all_points <- R[which(R$refx == r),]
        lines(all_points$refy ~ all_points$refx, col="lightgray")
    }

    # Connect all points in row
    rows <- unique(R$refy)
    for (r in rows) {
        all_points <- R[which(R$refy == r),]
        lines(all_points$refy ~ all_points$refx, col="lightgray")
    }

    rows <- unique(R$refx)
    for (r in rows) {
        all_points <- R[which(R$refx == r),]
        lines(all_points$posy ~ all_points$posx, col="blue")
    }

    # Connect all points in row
    rows <- unique(R$refy)
    for (r in rows) {
        all_points <- R[which(R$refy == r),]
        lines(all_points$posy ~ all_points$posx, col="blue")
    }
}

#' Calculates distortion using a 6-parameter model.
#' 
#' @param R data.frame with refx, refy, posx, posy columns
#' @return data vector holding the calculated coeffients
calc_distortion <- function(R) {
    # 6 param model
    dx<-R$posx-R$refx
    dy<-R$posy-R$refy
                  #1  #3       #5        
    xm <- lm(dx ~     R$refx + R$refy)
    
                  #2  #4       #6
    ym <- lm(dy ~     R$refy + R$refx)

    k1 <- xm$coefficients[1]
    k2 <- ym$coefficients[1]
    
    k3 <- xm$coefficients[2]
    k4 <- ym$coefficients[2]
    
    k5 <- xm$coefficients[3]
    k6 <- ym$coefficients[3]
    
    k <- c(k1,k2,k3,k4,k5,k6)   
    names(k) <- unlist(lapply(1:6,FUN=function(a){paste0("k",a)}))
    k
}

#' Applies the distortion described by the provided coefficients to the given grid.
#' 
#' @param R data.frame with refx, refy, posx, posy columns
#' @return data.frame with refx, refy, posx, posy columns; diffx and diffy columns will be created if not existing.
apply_correction <- function(R, coeffs) {
    dx <- coeffs[1] + coeffs[3]*R$refx + coeffs[5] * R$refy
    dy <- coeffs[2] + coeffs[4]*R$refy + coeffs[6] * R$refx
    
    distorted <- as.data.frame(R)
    distorted$posx <- R$posx - dx
    distorted$posy <- R$posy - dy
    distorted$diffx <- dx
    distorted$diffy <- dy
    distorted
}

#' Applies the higher order distortion described by the provided coefficients to the given grid.
#' @description
#' Supports higher order, first quadratic term.
#' 
#' @param R data.frame with refx, refy, posx, posy columns
#' @return data.frame with refx, refy, posx, posy columns; diffx and diffy columns will be created if not existing.
apply_higher_order_correction <- function(R, coeffs) {
    dx <- coeffs[1] + coeffs[3]*R$refx + coeffs[5] * R$refy + coeffs[7]*(R$refx*R$refx) + coeffs[ 9]*(R$refx*R$refy)  
    dy <- coeffs[2] + coeffs[4]*R$refy + coeffs[6] * R$refx + coeffs[8]*(R$refy*R$refy) + coeffs[10]*(R$refy*R$refx)
    
    distorted <- as.data.frame(R)
    distorted$posx <- R$posx - dx
    distorted$posy <- R$posy - dy
    distorted$diffx <- dx
    distorted$diffy <- dy
    distorted
}

#' Creates a 13x13 grid which can be then distorted using given high order coefficients.
#' @param coeffs coefficients
#' @return data.frame with refx, refy, posx, posy columns; diffx and diffy for differences 
distort <- function(coeffs) {
    if (length(coeffs)<10) {
        stop("Insufficient number of coefficients. At least 10 coefficients needed.")
    }
    
    X<-generate_grid(l=-6,u=6,noise=0)
    D<-apply_higher_order_correction(X, coeffs)
    
    ox <- mean(D$posx-D$refx)
    oy <- mean(D$posy-D$refy)
    
    D$posx <- D$posx - ox
    D$posy <- D$posy - oy
    D$diffx <- D$posx - D$refx
    D$diffy <- D$posy - D$refy
        
    plot_distortion_grid(D,margin=.4,main_title="HO ignoring translation")
}

# Some Demo Code
if (FALSE) {
    
    setwd(dirname(file.path(file.choose())))
    
    plot_distortion_grid(generate_grid(noise=200))

    png("grid_plot_1.png")
    plot_distortion_grid(generate_grid(l=-2.5,u=2.5,noise=100))
    dev.off()

    png("grid_plot_2.png")
    plot_distortion_grid(generate_grid(l=-4.5,u=4.5,noise=100))
    dev.off()

    png("grid_plot_3.png")
    plot_distortion_grid(generate_grid(l=-7.5,u=7.5,noise=100))
    dev.off()

    
    png("distortions_noise_only.png")
    with_noise<-generate_grid(l=-2.5,u=2.5,noise=80)
    coeffs <- calc_distortion(with_noise)
    noisy <- apply_correction(with_noise,coeffs)
    plot_distortion_grid(noisy,margin=.4,main_title="Noise Only")
    dev.off()
        
    png("distortions_none.png")
    zero_noise<-generate_grid(l=-2.5,u=2.5,noise=0)
    distorted <- apply_correction(zero_noise,c(0,0,0,0,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="No distortion")
    dev.off()
        
    # Translation
    shift <- 500
    
    png("distortions_translation_x.png")
    distorted <- apply_correction(zero_noise,c(shift,0,0,0,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Translation X")
    dev.off()

    png("distortions_translation_y.png")
    distorted <- apply_correction(zero_noise,c(0,shift,0,0,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Translation Y")
    dev.off()
    
    png("distortions_translation_xy.png")
    distorted <- apply_correction(zero_noise,c(shift,shift,0,0,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Translation X+Y")
    dev.off()
    
    
    # Scaling (shrinking)
    scale <- .2
    
    png("distortions_scaling_shrink_x.png")
    distorted <- apply_correction(zero_noise,c(0,0,scale,0,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling (shrinked) X")
    dev.off()

    png("distortions_scaling_shrink_y.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,scale,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling (shrinked) Y")
    dev.off()
    
    png("distortions_scaling_shrink_xy.png")
    distorted <- apply_correction(zero_noise,c(0,0,scale,scale,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling (shrinked) X+Y")
    dev.off()
    
    # Scaling (extending)
    
    png("distortions_scaling_extend_x.png")
    distorted <- apply_correction(zero_noise,c(0,0,-scale,0,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling (extended) X")
    dev.off()

    png("distortions_scaling_extend_y.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,-scale,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling (extended) Y")
    dev.off()
    
    png("distortions_scaling_extend_xy.png")
    distorted <- apply_correction(zero_noise,c(0,0,-scale,-scale,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling (extended) X+Y")
    dev.off()

    
    # Skew
    
    skew <- .15
    png("distortions_skew_x.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,skew,0))
    plot_distortion_grid(distorted,margin=.4,main_title=paste0("Skew X ",skew))
    dev.off()
    
    png("distortions_skew_x_inv.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,-skew,0))
    plot_distortion_grid(distorted,margin=.4,main_title=paste0("Skew X ",-skew))
    dev.off()
    
    png("distortions_skew_y.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,0,skew))
    plot_distortion_grid(distorted,margin=.4,main_title=paste0("Skew Y ",skew))
    dev.off()
    
    png("distortions_skew_y_inv.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,0,-skew))
    plot_distortion_grid(distorted,margin=.4,main_title=paste0("Skew Y ",-skew))
    dev.off()
    
    png("distortions_skew_xy.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,skew,skew))
    plot_distortion_grid(distorted,margin=.4,main_title="Skew X+Y")
    dev.off()
    
    png("distortions_skew_xinv_y.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,-skew,skew))
    plot_distortion_grid(distorted,margin=.4,main_title="Skew -X+Y")
    dev.off()
    
    png("distortions_skew_x_yinv.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,skew,-skew))
    plot_distortion_grid(distorted,margin=.4,main_title="Skew X-Y")
    dev.off()

    png("distortions_skew_xinv_yinv.png")
    distorted <- apply_correction(zero_noise,c(0,0,0,0,-skew,-skew))
    plot_distortion_grid(distorted,margin=.4,main_title="Skew -X-Y")
    dev.off()
    
    # Scaling + Shifting
    
    png("distortions_scaling_shifting_xy.png")
    distorted <- apply_correction(zero_noise,c(shift*.8,-shift/3,scale,scale/2,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling + Shifting X+Y")
    dev.off()
    
    # Noisy + Scaling + Shifting
    png("distortions_noisy_scaling_shifting_xy.png")
    distorted <- apply_correction(noisy,c(shift*.8,-shift/3,scale,scale/2,0,0))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling + Shifting + Noise X+Y")
    dev.off()
    
    # Scaling + Shifting + Skew
    png("distortions_noisy_scaling_shifting_skew_xy.png")
    distorted <- apply_correction(noisy,c(shift*.8,-shift/3,scale,scale/2,skew/2,-2*skew))
    plot_distortion_grid(distorted,margin=.4,main_title="Scaling + Shifting + Noise + Skew X+Y")
    dev.off()
}

