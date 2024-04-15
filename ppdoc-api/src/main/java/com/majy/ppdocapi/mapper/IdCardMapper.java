package com.majy.ppdocapi.mapper;

import com.majy.ppdocapi.pojo.IdCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdCardMapper
{
    @Select("select * from id_card")
    public List<IdCard> list();
}
