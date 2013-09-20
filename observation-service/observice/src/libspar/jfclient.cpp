

#include "jfrecord.h"
#include "jfclient.h"


namespace jsrfilter
{

  tcp_client::tcp_client(boost::asio::io_service& io_service,
			 const std::string&       host,
			 const std::string&       port)
    : io_service_(io_service),
      socket_(io_service),
      host_(host),
      port_(port)
  {
    
  }


  void tcp_client::connect()
  {
    tcp::resolver resolver(io_service_);
    tcp::resolver::query query(host_, port_, tcp::resolver::query::numeric_service);
    tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);
    tcp::resolver::iterator end;

    boost::system::error_code error = boost::asio::error::host_not_found;
    while (error && endpoint_iterator != end)
    {
      socket_.close();
      socket_.connect(*endpoint_iterator++, error);
    }
    if (error)
      throw boost::system::system_error(error);

  }

  void tcp_client::publish(const std::string& topic,
			   const jfrecord&    record)
  {
    std::stringstream ss;
    ss << record;
    message_ = ss.str();
    boost::asio::async_write(socket_, boost::asio::buffer(message_),
			     boost::bind(&tcp_client::handle_write, shared_from_this(),
					 boost::asio::placeholders::error,
					 boost::asio::placeholders::bytes_transferred));
  }

  void tcp_client::close()
  {
    socket_.close();
  }


  void tcp_client::handle_write(const boost::system::error_code& /*error*/,
				size_t /*bytes_transferred*/)
  {
    
  }


}
