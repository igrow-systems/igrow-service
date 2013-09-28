/* -*- mode: c++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

#if !defined(__JSRFILTER_LIBJF_TCP_CLIENT)
#define __JSRFILTER_LIBJF_TCP_CLIENT

#include <ctime>
#include <iostream>
#include <string>
#include <boost/bind.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/enable_shared_from_this.hpp>
#include <boost/asio.hpp>

namespace
{
  std::string make_daytime_string()
  {
    using namespace std; // For time_t, time and ctime;
    time_t now = time(0);
    return ctime(&now);
  }
}

using boost::asio::ip::tcp;

namespace jsrfilter
{

class jfrecord;

class tcp_client
  : public boost::enable_shared_from_this<tcp_client>
{
public:
  typedef boost::shared_ptr<tcp_client> pointer;

  static pointer create(boost::asio::io_service& io_service,
                        const std::string&       host,
                        const std::string&       port)
  {
    return pointer(new tcp_client(io_service, host, port));
  }

  void connect();
  void subscribe();
  void publish(const std::string& topic,
               const jfrecord&    record);
  void close();

private:
  tcp_client(boost::asio::io_service& io_service,
	     const std::string&       host,
	     const std::string&       port);

  void handle_write(const boost::system::error_code& /*error*/,
                    size_t /*bytes_transferred*/);


  boost::asio::io_service& io_service_;
  tcp::socket              socket_;
  const std::string        host_;
  const std::string        port_;
  std::string              message_;
};

}

#endif  //__JSRFILTER_LIBJF_TCP_CLIENT
