import winston from 'winston';
import winston_drf from 'winston-daily-rotate-file'; // log 파일을 일자별로 생성하기 위해 사용
//import time from 'time' // 로그파일 제목에 일자를 표시하기 위해 사용
import path from 'path';

const logdir = path.join(__dirname,'../Log');

const infoTransport = new winston.transports.DailyRotateFile({
  filename: `%DATE%_info.log`,
  dirname: logdir + '/info',
  level:'info',
  datePatten: 'YYYY-MM-DD-HH',
  timestamp: function () {
    var timezone = time.currentTimezone
    var now = new time.Date()
    now.setTimezone(timezone)
    return now.toString()
  }
});

const errorTransport = new winston.transports.DailyRotateFile({
    filename: `%DATE%_error.log`,
    dirname: logdir + '/error',
    level:'error',
    datePatten: 'YYYY-MM-DD-HH',
    timestamp: function () {
        var timezone = time.currentTimezone
        var now = new time.Date()
        now.setTimezone(timezone)
        
        return now.toString()
  }
})

export const logger = winston.createLogger({
  transports: [
    infoTransport,
    errorTransport
  ]
});

export const stream = {
    write: (message) => {
        logger.info(message);
    }
}

export default logger;