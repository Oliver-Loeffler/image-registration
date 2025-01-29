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
plot_distortion_grid <- function(R, margin=0.1) {
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
         main = "Displacement Grid Plot", col="lightgray", pch=3, lwd=0.1, cex=0.1, xlim=x_extent, ylim=y_extent)

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


# Some Demo Code
if (FALSE) {
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
}

