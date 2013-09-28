/* -*- mode: c++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */


/*
 * @(#)TcpConnection.h        
 *
 * Copyright (c) 2013 Argusat Limited
 * 10 Underwood Road, Southampton.  UK
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Argusat Limited. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Argusat Limited.
 */

#if !defined(ARGUSAT_GJL_OBSERVICE_TCP_CONNECTION_H_)
#define ARGUSAT_GJL_OBSERVICE_TCP_CONNECTION_H_

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

namespace argusat {
namespace gjl {
namespace observice {

class TcpConnection
  : public boost::enable_shared_from_this<TcpConnection>
{
public:
  typedef boost::shared_ptr<TcpConnection> TcpConnectionPtr;

  static TcpConnectionPtr create(boost::asio::io_service& io_service)
  {
    return TcpConnectionPtr(new TcpConnection(io_service));
  }

  boost::asio::ip::tcp::socket& socket()
  {
    return socket_;
  }

  void start()
  {
    message_ = make_daytime_string();

    boost::asio::async_write(socket_, boost::asio::buffer(message_),
        boost::bind(&TcpConnection::handle_write, shared_from_this(),
          boost::asio::placeholders::error,
          boost::asio::placeholders::bytes_transferred));
  }

private:
  TcpConnection(boost::asio::io_service& io_service)
    : socket_(io_service)
  {
  }

  void handle_write(const boost::system::error_code& /*error*/,
      size_t /*bytes_transferred*/)
  {
  }

private:
  boost::asio::ip::tcp::socket socket_;
  std::string                  message_;
};

}  // end namespace observice
}  // end namespace gjl
}  // end namespace argusat

#endif // ARGUSAT_GJL_OBSERVICE_TCP_CONNECTION_H_
