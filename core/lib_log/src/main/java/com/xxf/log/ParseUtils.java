package com.xxf.log;

import com.xxf.log.parser.Parser;

import java.util.List;

class ParseUtils {

    static String dispatch(List<Parser> parserList, Object in){
        for(int i=0;i<parserList.size();i++){
            Parser parser = parserList.get(i);
            if(parser.parseClassType().isAssignableFrom(in.getClass())){
                return  parser.parseString(in);
            }
        }
        return ""+in;
    }
}
