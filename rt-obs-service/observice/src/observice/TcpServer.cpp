/* -*- mode: c++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)TcpServer.h        
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


#include <sstream>

#include <log4cxx/logger.h>
#include <boost/asio.hpp>

#include "TcpConnection.h"


namespace argusat {
namespace gjl {
namespace observice {

TcpServer::TcpServer(boost::asio::io_service& io_service)
    : acceptor_(io_service, boost::asio::ip::tcp::endpoint(boost::asio::ip::tcp::v4(), 49100))
{
  logger_ = log4cxx::Logger::getLogger(kClassName);
  StartAccept();
}

void TcpServer::StartAccept()
{
  TcpConnection::TcpConnectionPtr new_connection =
    TcpConnection::create(acceptor_.get_io_service());

  acceptor_.async_accept(new_connection->socket(),
			 boost::bind(&TcpServer::HandleAccept, this, new_connection,
				     boost::asio::placeholders::error));
}

void TcpServer::HandleAccept(const TcpConnection::TcpConnectionPtr& new_connection,
			     const boost::system::error_code&       error)
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
      {
	break; // Connection closed cleanly by peer.
      }
      else if (error)
      {
	throw boost::system::system_error(error); // Some other error.
      }

      ss.write(buf.data(), len);
      LOG4CXX_TRACE(logger_, ss.str());
      ss.flush();
    }

    StartAccept();
  }
}


}  // end namespace observice
}  // end namespace gjl
}  // end namespace argusat

