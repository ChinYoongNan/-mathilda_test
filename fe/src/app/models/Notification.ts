import { getDateFromTimestamp } from '../helpers/index';
export default class Notification {
  id: string;
  title: string;
  message: string;
  type: string;
  icon: string;
  status: string;
  timestamp: string;
  userId: string;
  links: NotiLink[];
  service: string;
  roleId: string;
  date: string;
  constructor(id, title, message, messageType, status, date, timestamp, icon) {
    this.id = id || '';
    this.title = title || '';
    this.message = message || '';
    this.type = messageType || '';
    this.status = status || '';
    this.timestamp = timestamp || '';
    this.icon = icon || '';
    this.date = date || '' + getDateFromTimestamp(this.timestamp);
  }
}

export class NotiLink {
  key: string;
  link: string;
  value: string;
}
