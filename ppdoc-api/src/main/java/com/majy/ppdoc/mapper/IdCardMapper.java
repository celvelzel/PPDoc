package com.majy.ppdoc.mapper;

import com.majy.ppdoc.pojo.IdCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IdCardMapper
{
    @Select("select * from id_card")
    public List<IdCard> list();
}
