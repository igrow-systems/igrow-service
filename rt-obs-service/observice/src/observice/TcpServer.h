/* -*- mode: c++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)tcp_server.h        
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

#if !defined(ARGUSAT_GJL_OBSERVICE_TCP_SERVER_H_)
#define ARGUSAT_GJL_OBSERVICE_TCP_SERVER_H_

#include <sstream>

#include <log4cxx/logger.h>
#include <boost/asio.hpp>

#include "TcpConnection.h"


namespace argusat {
namespace gjl {
namespace observice {

class TcpServer
{
public:
  TcpServer(boost::asio::io_service& io_service);

private:
  void StartAccept()
  void HandleAccept(const TcpConnection::TcpConnectionPtr& new_connection,
                    const boost::system::error_code&       error)

private:
  log4cxx::LoggerPtr             logger_;
  boost::asio::ip::tcp::acceptor acceptor_;
};

}  // end namespace observice
}  // end namespace gjl
}  // end namespace argusat

#endif // ARGUSAT_GJL_TCP_SERVER_H_
