import { find } from 'lodash';
import { getMonth, getYear, toDate, format, isToday, differenceInMinutes, differenceInHours } from 'date-fns';

export function getCookieByName(name) {
  const value = '; ' + document.cookie;
  const parts = value.split('; ' + name + '=');
  if (parts.length === 2) {
    return parts.pop().split(';').shift();
  }
  return null;
}

export function stringContains(text: string, criteria: string) {
  return text.toLowerCase().indexOf(criteria.toLowerCase()) > -1;
}

export function isValidUUID(uuid: string) {
  const guidPattern = /[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}/i;
  return uuid.match(guidPattern);
}

export function getUuidType() {
  return 'UUID';
}

export function getCurrentYear() {
  return getYear(new Date());
}

export function getCurrentMonth() {
  return getMonth(new Date()) + 1;
}

export function hasAccess(method_name: string) {
  const { access } = JSON.parse(localStorage.getItem('rootAuthenticate'));
  if (!access || access.length < 1) {
    return false;
  }

  return find(access, (a) => a && a.method_name === method_name) != undefined;
}

export function getDateFromTimestamp(timestamp) {
  return format(toDate(timestamp), 'MMM dd, hh:mm aa');
}

export function formatNotificationDateFromTimestamp(timestamp) {
  const date = toDate(timestamp);
  const minutesDiffer = differenceInMinutes(new Date(), date);
  if (minutesDiffer < 1) {
    return 'Just now';
  } else if (minutesDiffer < 2) {
    return '1 minutes ago';
  } else if (minutesDiffer < 3) {
    return '2 minutes ago';
  } else if (minutesDiffer < 60) {
    return `${minutesDiffer} minutes ago`;
  } else if (minutesDiffer < 120) {
    return '1 hour ago';
  }

  const hourDiffer = differenceInHours(new Date(), date);
  if ( hourDiffer < 24) {
    return `${hourDiffer} hours ago`;
  } else if (hourDiffer < 48) {
    return 'Yesterday';
  }

  return format(toDate(timestamp), 'MMM dd');
}
