/* -*- mode: c++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */

#if !defined(__JSRFILTER_LIBJF_JFRECORD)
#define __JSRFILTER_LIBJF_JFRECORD

#include <string>
#include <iostream>

namespace jsrfilter
{

  class jfrecord
  {
    //    friend class jfclient;
 
  public:
    jfrecord();

    friend std::ostream& operator<<(std::ostream& os, const jfrecord& record);

  private:

    template<typename V>
    void set(const std::string& key, const V& value);

  };

}

#endif  //__JSRFILTER_LIBJF_JFRECORD
