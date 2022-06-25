package com.narangnorang.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.narangnorang.dto.ChallengeDTO;
import com.narangnorang.dto.DailyLogDTO;
import com.narangnorang.dto.MoodStateDTO;

@Repository("chatBotDAO")
public class ChatBotDAO {

	@Autowired
	SqlSession sqlSession;

	// 챌린지 조회(하루)
	public ChallengeDTO selectChallenge(ChallengeDTO challengeDTO) throws Exception {
		return sqlSession.selectOne("com.config.ChatBotMapper.selectChallenge", challengeDTO);
	}

	// 일일 데이터 조회(하루)
	public DailyLogDTO selectDailyLog(DailyLogDTO dailyLogDTO) {
		return sqlSession.selectOne("com.config.ChatBotMapper.selectDailyLog", dailyLogDTO);
	}

	// 기분 상태 조회(접속일시 이후)
	public MoodStateDTO selectMoodState(MoodStateDTO moodStateDTO) throws Exception {
		return sqlSession.selectOne("com.config.ChatBotMapper.selectMoodState", moodStateDTO);
	}

	// 챌린지 저장
	public int insertChallenge(ChallengeDTO challengeDTO) throws Exception {
		return sqlSession.insert("com.config.ChatBotMapper.insertChallenge", challengeDTO);
	}

	// 일일 데이터 저장
	public int insertDailyLog(DailyLogDTO dailyLogDTO) throws Exception {
		return sqlSession.insert("com.config.ChatBotMapper.insertDailyLog", dailyLogDTO);
	}

	// 기분 상태 저장
	public int insertMoodState(MoodStateDTO moodStateDTO) throws Exception {
		return sqlSession.insert("com.config.ChatBotMapper.insertMoodState", moodStateDTO);
	}

	// 챌린지 보상
	public int updatePoint(ChallengeDTO challengeDTO) throws Exception {
		return sqlSession.insert("com.config.ChatBotMapper.updatePoint", challengeDTO);
	}

}
