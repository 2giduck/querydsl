package topia.duck.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import topia.duck.querydsl.dto.MemberSearchCondition;
import topia.duck.querydsl.dto.MemberTeamDto;
import topia.duck.querydsl.dto.QMemberTeamDto;
import topia.duck.querydsl.entity.Member;
import topia.duck.querydsl.entity.QMember;
import topia.duck.querydsl.entity.QTeam;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public List<Member> findAll_Querydsl(){
        return queryFactory
                .selectFrom(QMember.member)
                .fetch();
    }

    public List<Member> findByUsername_Querydsl(String username){
        return queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq(username))
                .fetch();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){
        BooleanBuilder builder = new BooleanBuilder();
        if(hasText(condition.getUsername())){
            builder.and(QMember.member.username.eq(condition.getUsername()));
        }
        if(hasText(condition.getTeamName())){
            builder.and(QTeam.team.name.eq(condition.getTeamName()));
        }
        if(condition.getAgeGoe() != null){
            builder.and(QMember.member.age.goe(condition.getAgeGoe()));
        }
        if(condition.getAgeLoe() != null){
            builder.and(QMember.member.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                        QMember.member.id.as("memberId"),
                        QMember.member.username,
                        QMember.member.age,
                        QTeam.team.id.as("teamId"),
                        QTeam.team.name.as("teamName")
                ))
                .from(QMember.member)
                .where(builder)
                .leftJoin(QMember.member.team, QTeam.team)
                .fetch();
    }

    public List<MemberTeamDto> search(MemberSearchCondition condition){
        return queryFactory
                .select(new QMemberTeamDto(
                        QMember.member.id.as("memberId"),
                        QMember.member.username,
                        QMember.member.age,
                        QTeam.team.id.as("teamId"),
                        QTeam.team.name.as("teamName")
                ))
                .from(QMember.member)
                .leftJoin(QMember.member.team, QTeam.team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? QMember.member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? QTeam.team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : QMember.member.age.goe(ageGoe);
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : QMember.member.age.loe(ageLoe);
    }
}
