import { Component, input } from '@angular/core';

@Component({
  selector: 'app-user-avatar',
  imports: [],
  templateUrl: './user-avatar.html',
})
export class UserAvatar {

  contributorInitials = input<string>('')
  contributorName = input<string>('')

}