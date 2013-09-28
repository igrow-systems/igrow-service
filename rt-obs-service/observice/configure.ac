# @start 1
# configure.in -- Process this file with autoconf to produce configure
# @end 1
# Copyright (C) 2000, 2006 Gary V. Vaughan
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2, or (at your option)
# any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
# MA 02110-1301, USA.

# @start 2

AC_PREREQ([2.68])

AC_INIT([observice], [0.0.1], [jsr@argusat.com])
AC_CONFIG_HEADERS([config.h:config-h.in])
AC_CONFIG_SRCDIR([src/observice/ObservationService.cpp])
AC_CONFIG_AUX_DIR([config])
AC_CONFIG_MACRO_DIR([m4])

AM_INIT_AUTOMAKE([1.9 foreign])

AC_PROG_CXX
AC_PROG_LIBTOOL

PKG_CHECK_MODULES([log4cxx], [liblog4cxx >= 0.10.0])
#PKG_CHECK_MODULES([Swiften], [libSwiften >= 2.0.0])


AX_PTHREAD

AX_BOOST_BASE([1.46.0])
AX_BOOST_SYSTEM
#AX_BOOST_FILESYSTEM
AX_BOOST_THREAD
AX_BOOST_ASIO

AC_CONFIG_FILES([Makefile src/Makefile src/observice/Makefile src/obsub/Makefile src/libspar/Makefile],
	[test -z "$CONFIG_HEADERS" || echo timestamp > src/observice/stamp-h.in])
AC_OUTPUT
# @end 2
