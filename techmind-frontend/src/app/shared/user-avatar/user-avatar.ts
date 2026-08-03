import { Component, input } from '@angular/core';
import { User } from '@primeicons/angular/user';

@Component({
  selector: 'app-user-avatar',
  imports: [User],
  templateUrl: './user-avatar.html',
})
export class UserAvatar {

  contributorInitials = input<string>('')

  contributorName = input<string>('')

}