
library("rgdal")
library("spacetime")

dsn <- "PG:dbname=argusat-gjl-dev user=argusat-gjl-dev password=argusat-gjl-dev host=localhost port=5432"
obs <- readOGR(dsn=dsn, 'observations')

gnssobs <- subset(obs, obs_type == 'TYPE_GNSS_CHANNEL')
gnssobs@data$obs_timestamp <- as.POSIXlt(gnssobs@data$obs_timestamp)
gnssobs@data$sensor_id <- as.factor(gnssobs@data$sensor_id)


rawdata <- gnssobs@data[,c(8,9,10)]
#gnssobs@data <- gnssobs@data[,!(names(gnssobs@data) %in% c("value0", "value1", "value2"))]

#gnssobs.st <- STIDF(gnssobs, gnssobs@data$obs_timestamp, rawdata)

for (sensorid in as.character(gnssobs@data$sensor_id)) {
  gnssobs.si <- subset(gnssobs, sensor_id == sensorid)
  rawdata.si <- gnssobs.si@data[,c(8,9,10)]
  gnssobs.si@data <- gnssobs.si@data[,!(names(gnssobs.si@data) %in% c("value0", "value1", "value2"))]
  assign(paste('gnssobs.sist', sensorid, sep=''), STIDF(gnssobs.si, gnssobs.si@data$obs_timestamp, rawdata.si))
}
