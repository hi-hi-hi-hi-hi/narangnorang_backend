package com.narangnorang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narangnorang.dao.NorangDAO;
import com.narangnorang.dto.ChallengeDTO;
import com.narangnorang.dto.DailyLogDTO;
import com.narangnorang.dto.MoodStateDTO;

@Service("norangService")
public class NorangServiceImpl implements NorangService {

	@Autowired
	NorangDAO norangDAO;

	// 챌린지 조회(하루)
	@Override
	public ChallengeDTO selectChallenge(ChallengeDTO challengeDTO) throws Exception {
		return norangDAO.selectChallenge(challengeDTO);
	}

	// 일일 데이터 조회(하루)
	@Override
	public DailyLogDTO selectDailyLog(DailyLogDTO dailyLogDTO) throws Exception {
		return norangDAO.selectDailyLog(dailyLogDTO);
	}

	// 기분 상태 조회(접속일시 이후)
	@Override
	public MoodStateDTO selectMoodState(MoodStateDTO moodStateDTO) throws Exception {
		return norangDAO.selectMoodState(moodStateDTO);
	}

	// 챌린지 저장
	@Transactional
	@Override
	public int insertChallenge(ChallengeDTO challengeDTO) throws Exception {
		norangDAO.updatePoint(challengeDTO); // 챌린지 보상
		return norangDAO.insertChallenge(challengeDTO);
	}

	// 일일 데이터 저장
	@Override
	public int insertDailyLog(DailyLogDTO dailyLogDTO) throws Exception {
		return norangDAO.insertDailyLog(dailyLogDTO);
	}

	// 기분 상태 저장
	@Override
	public int insertMoodState(MoodStateDTO moodStateDTO) throws Exception {
		return norangDAO.insertMoodState(moodStateDTO);
	}

}
