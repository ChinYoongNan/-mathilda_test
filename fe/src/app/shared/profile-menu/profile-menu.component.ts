import { Component, OnInit } from '@angular/core';
import { ROOT_USER_LOGOUT, ROOT_USER_TO_USER_PROFILE } from '../../app.actions';
import { Store } from '@ngrx/store';
import { State } from '../../app.reducers';

@Component({
  selector: 'app-profile-menu',
  templateUrl: './profile-menu.component.html'
})
export class ProfileMenuComponent implements OnInit {
  public profileMenuItems: any[] = [
    {
      label: 'Profile',
      icon: 'account_box',
      action: this.navigateToUserProfile.bind(this)
    },
    {
      label: 'Sign Out',
      icon: 'exit_to_app',
      action: this.signOut.bind(this)
    }
  ];

  constructor(private store: Store<State>) {}

  ngOnInit() {}

  public signOut() {
    this.store.dispatch({ type: ROOT_USER_LOGOUT, payload: '' });
  }

  public navigateToUserProfile() {
    this.store.dispatch({ type: ROOT_USER_TO_USER_PROFILE, payload: '' });
  }
}
