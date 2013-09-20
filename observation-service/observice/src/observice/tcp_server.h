/* -*- mode: c++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

#if !defined(__JSRFILTER_JSRFILTER_SERVER)
#define __JSRFILTER_JSRFILTER_SERVER

#include "log4cxx/logger.h"

#include <boost/asio.hpp>

#include "tcp_connection.h"

using boost::asio::ip::tcp;
using namespace log4cxx;
using namespace log4cxx::helpers;

namespace jsrfilter
{

class tcp_server
{
  public:
  tcp_server(boost::asio::io_service& io_service)
    : acceptor_(io_service, tcp::endpoint(tcp::v4(), 49100))
  {
    logger_ = Logger::getLogger("jsrfilter.tcp_server");
    start_accept();
  }

private:
  void start_accept()
  {
    tcp_connection::pointer new_connection =
      tcp_connection::create(acceptor_.get_io_service());

    acceptor_.async_accept(new_connection->socket(),
        boost::bind(&tcp_server::handle_accept, this, new_connection,
          boost::asio::placeholders::error));
  }

  void handle_accept(tcp_connection::pointer new_connection,
      const boost::system::error_code& error)
  {
    std::stringstream ss;
    ss << "Accepted incoming client connection from: "
       << new_connection->socket().remote_endpoint();
    LOG4CXX_INFO(logger_, ss.str());
    if (!error)
    {
      new_connection->start();
      
      std::stringstream ss;
      for (;;)
      {
        boost::array<char, 128> buf;
        boost::system::error_code error;

        size_t len = new_connection->socket().read_some(boost::asio::buffer(buf), error);

        if (error == boost::asio::error::eof)
          break; // Connection closed cleanly by peer.
        else if (error)
          throw boost::system::system_error(error); // Some other error.

        ss.write(buf.data(), len);
        LOG4CXX_TRACE(logger_, ss.str());
        ss.flush();
      }



      start_accept();
    }
  }

  LoggerPtr logger_;

  tcp::acceptor acceptor_;
};

}

#endif
