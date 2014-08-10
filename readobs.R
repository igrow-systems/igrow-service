
library("rgdal")
library("spacetime")
library("ggplot2")
library("gstat")

dsn <- "PG:dbname=argusat-gjl-dev user=argusat-gjl-dev password=argusat-gjl-dev host=localhost port=5432"
obs <- readOGR(dsn=dsn, 'observations')

gnssobs <- subset(obs, obs_type == 'TYPE_GNSS_CHANNEL')
gnssobs@data$obs_timestamp <- as.POSIXlt(gnssobs@data$obs_timestamp)
gnssobs@data$sensor_id <- as.factor(gnssobs@data$sensor_id)


rawdata <- gnssobs@data[,c(8,9,10)]
#gnssobs@data <- gnssobs@data[,!(names(gnssobs@data) %in% c("value0", "value1", "value2"))]

#gnssobs.st <- STIDF(gnssobs, gnssobs@data$obs_timestamp, rawdata)

start.time <- system.time()

for (sensorid in as.character(levels(gnssobs@data$sensor_id))) {

  gnssobs.si <- subset(gnssobs, sensor_id == sensorid)
  assign(paste('gnssobs.si', sensorid, sep=''), gnssobs.si)
  rawdata.si <- gnssobs.si@data[,c(8,9,10)]
  #gnssobs.si@data <- gnssobs.si@data[,!(names(gnssobs.si@data) %in% c("value0", "value1", "value2"))]
  #assign(paste('gnssobs.sist', sensorid, sep=''), STIDF(gnssobs.si, gnssobs.si@data$obs_timestamp, rawdata.si))
  # Create a grid
  gnssobs.grid <- expand.grid( x=seq(from=gnssobs.si@bbox[1,1], to=gnssobs.si@bbox[1,2], length.out=1000), y=seq(from=gnssobs.si@bbox[2,1], to=gnssobs.si@bbox[2,2], length.out=1000))
  coordinates(gnssobs.grid) <- ~x+y
  gnssobs.grid <- SpatialPoints(gnssobs.grid, proj4string=gnssobs.si@proj4string)
  gridded(gnssobs.grid) <- TRUE   # casts to SpatialPixels

  assign(paste('gnssobs.si', sensorid, '.grid', sep=''), gnssobs.grid)
  
  # Do inverse distance weighting interpolation
  idw.out <- gstat::idw(value2 ~ 1, gnssobs.si, gnssobs.grid)
  
  idw.out.df <- as.data.frame(idw.out)
  assign(paste('idw.out.df', sensorid, sep=''), idw.out.df)
}

end.time <- system.time()
print(paste('Took ', end.time - start.time, ' s', sep=''))

# Create a grid
gnssobs.si17.grid <- expand.grid( x=seq(from=gnssobs.si17@bbox[1,1], to=gnssobs.si17@bbox[1,2], by=0.0001), y=seq(from=gnssobs.si17@bbox[2,1], to=gnssobs.si17@bbox[2,2], by=0.0001))
coordinates(gnssobs.si17.grid) <- ~x+y
gnssobs.si17.grid <- SpatialPoints(gnssobs.si17.grid, proj4string=gnssobs.si17@proj4string)
gridded(gnssobs.si17.grid) <- TRUE   # casts to SpatialPixels

plot(gnssobs.si17.grid)
points(gnssobs.si17, pch = 1, col = "red", cex = 1)

# Do inverse distance weighting interpolation
idw.out <- gstat::idw(value2 ~ 1, gnssobs.si17, gnssobs.si17.grid)

idw.out.df <- as.data.frame(idw.out)
#names(idw.out.df)[1:2] <- c("long", "lat")

plot <- ggplot(data = idw.out.df18, aes(x = x, y = y))  #start with the base-plot
layer1 <- c(geom_tile(data = idw.out.df18, aes(fill = var1.pred)))  #then create a tile layer and fill with predicted values
plot + layer1 + scale_fill_gradient(low = "#FEEBE2", high = "#7A0177")




