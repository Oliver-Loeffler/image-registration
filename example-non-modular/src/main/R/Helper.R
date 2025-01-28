generate_sub_grid <- function(x, y_range) {
    data.frame(
        "x"=rep(x,length(y_range)),
        "y"=y_range
    )
}

add_noise <- function(sample, noise=1, offset=0) {
    if (noise>0) {
        return(sample+rnorm(1:length(sample), sd=noise))
    }
    return(sample)
}

generate_grid <- function(l=-2,u=2,pitch=1000, noise=1) {
    x_rng=(l:u)*pitch
    y_rng=(l:u)*pitch
    result <- do.call(rbind,lapply(x_rng, FUN=generate_sub_grid, y_rng))

    posx <- add_noise(result$x, noise)
    posy <- add_noise(result$y, noise)

    cbind(result,
         "posx"=posx,
         "posy"=posy,
         "dx"=posx - result$x,
         "dy"=posy - result$y)
}

plot_distortion_grid <- function(R) {
    # R must have:
    # x,y = Design Coordinates
    # posx, posy = Actual Coordinates
    # dx, dy = Displacement, e.g. dx = posx - x

    # Connect all points in column
    plot(R$posy ~ R$posx,
         xlab = "X position",
         ylab = "Y position",
         main = "Displacement Grid Plot")

    rows <- unique(R$x)
    for (r in rows) {
        all_points <- R[which(R$x == r),]
        lines(all_points$posy ~ all_points$posx)
    }

    # Connect all points in row
    rows <- unique(R$y)
    for (r in rows) {
        all_points <- R[which(R$y == r),]
        lines(all_points$posy ~ all_points$posx)
    }
}
plot_distortion_grid(generate_grid(noise=200))