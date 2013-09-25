/* -*- mode: C++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

/*
 * @(#)ObService.cpp        
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

// include log4cxx header files.
#include "log4cxx/logger.h"
#include "log4cxx/basicconfigurator.h"
#include "log4cxx/helpers/exception.h"

#include <boost/asio.hpp>

#include "tcp_server.h"

using namespace log4cxx;
using namespace log4cxx::helpers;

namespace
{
  LoggerPtr logger(Logger::getLogger("observice"));
}

int main(int argc, char **argv)
{
  int result = 0;
  try
  {
    // Set up a simple configuration that logs on the console.
    BasicConfigurator::configure();

    LOG4CXX_INFO(logger, "Entering application.");

    boost::asio::io_service io_service;
    //jsrfilter::tcp_server server(io_service);
    //io_service.run();

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
