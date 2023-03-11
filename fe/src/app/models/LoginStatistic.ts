import ActivityTimeline from './ActivityTimeline';

export default class LoginStatistic {
  total: number;
  details: ActivityTimeline[];

  constructor(total?: number, details?: ActivityTimeline[]) {
    if (total) {
      this.total = total;
    }
    if (details) {
      this.details = details;
    }
  }
}
