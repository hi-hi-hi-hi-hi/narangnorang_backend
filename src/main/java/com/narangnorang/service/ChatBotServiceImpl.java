package com.narangnorang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narangnorang.dao.ChatBotDAO;
import com.narangnorang.dto.ChallengeDTO;
import com.narangnorang.dto.DailyLogDTO;
import com.narangnorang.dto.MoodStateDTO;

@Service("chatBotService")
public class ChatBotServiceImpl implements ChatBotService {

	@Autowired
	private ChatBotDAO chatBotDAO;

	// 챌린지 조회(하루)
	@Override
	public ChallengeDTO selectChallenge(ChallengeDTO challengeDTO) throws Exception {
		return chatBotDAO.selectChallenge(challengeDTO);
	}

	// 일일 데이터 조회(하루)
	@Override
	public DailyLogDTO selectDailyLog(DailyLogDTO dailyLogDTO) throws Exception {
		return chatBotDAO.selectDailyLog(dailyLogDTO);
	}

	// 기분 상태 조회(접속일시 이후)
	@Override
	public MoodStateDTO selectMoodState(MoodStateDTO moodStateDTO) throws Exception {
		return chatBotDAO.selectMoodState(moodStateDTO);
	}

	// 챌린지 저장
	@Transactional
	@Override
	public int insertChallenge(ChallengeDTO challengeDTO) throws Exception {
		chatBotDAO.updatePoint(challengeDTO); // 챌린지 보상
		return chatBotDAO.insertChallenge(challengeDTO);
	}

	// 일일 데이터 저장
	@Override
	public int insertDailyLog(DailyLogDTO dailyLogDTO) throws Exception {
		return chatBotDAO.insertDailyLog(dailyLogDTO);
	}

	// 기분 상태 저장
	@Override
	public int insertMoodState(MoodStateDTO moodStateDTO) throws Exception {
		return chatBotDAO.insertMoodState(moodStateDTO);
	}

}
