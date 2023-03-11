import { InjectableRxStompConfig } from '@stomp/ng2-stompjs';
import * as SockJS from 'sockjs-client';
import { APP_CONFIG } from './cores/config';

/**
 * Monkey-patch for XHR with noCredentials to avoid CORS
 */
import AbstractXHRObject from 'sockjs-client/lib/transport/browser/abstract-xhr';
const _start = AbstractXHRObject.prototype._start;
AbstractXHRObject.prototype._start = function(method, url, payload, opts) {
  if (!opts) {
    opts = { noCredentials : true };
  }
  return _start.call(this, method, url, payload, opts);
};

export const rxStompConfig = (): InjectableRxStompConfig => {
  return {
    // How often to heartbeat?
    // Interval in milliseconds, set to 0 to disable
    heartbeatIncoming: 0, // Typical value 0 - disabled
    heartbeatOutgoing: 20000, // Typical value 20000 - every 20 seconds

    // Wait in milliseconds before attempting auto reconnect
    // Set to 0 to disable
    // Typical value 500 (500 milli seconds)
    reconnectDelay: 200,

    // Will log diagnostics on console
    // It can be quite verbose, not recommended in production
    // Skip this key to stop logging to console
    debug: (msg: string): void => {
      console.log(new Date(), msg);
    },

    webSocketFactory: () =>
      new SockJS(APP_CONFIG.secrets.api.socketEndpoint),
  };
};
