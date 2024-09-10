package com.meetsipdrink.ban.mapper;

import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.entity.Ban;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BanMapper {
    BanDto.Response banToBanResponse(Ban ban);
    List<BanDto.Response> banToBanResponseList(List<Ban> bans);
}
