
// include log4cxx header files.
#include "log4cxx/logger.h"
#include "log4cxx/basicconfigurator.h"
#include "log4cxx/helpers/exception.h"

#include <boost/asio.hpp>

#include "tcp_server.h"

using namespace log4cxx;
using namespace log4cxx::helpers;

LoggerPtr logger(Logger::getLogger("jsrfilter"));

int main(int argc, char **argv)
{
  int result = 0;
  try
  {
    // Set up a simple configuration that logs on the console.
    BasicConfigurator::configure();

    LOG4CXX_INFO(logger, "Entering application.");

    boost::asio::io_service io_service;
    jsrfilter::tcp_server server(io_service);
    io_service.run();

    LOG4CXX_INFO(logger, "Exiting application.");
  }
  catch(Exception&)
  {
    result = -1;
  }
  catch (std::exception& e)
  {
    std::cerr << e.what() << std::endl;
  }


  return result;
}
