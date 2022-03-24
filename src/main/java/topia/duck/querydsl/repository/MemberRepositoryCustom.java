package topia.duck.querydsl.repository;

import topia.duck.querydsl.dto.MemberSearchCondition;
import topia.duck.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
