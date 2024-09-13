import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-updateprofile',
  templateUrl: './updateprofile.component.html',
  styleUrls: ['./updateprofile.component.css']
})
export class UpdateprofileComponent {
  username: string = '';
  email: string = '';
  picture: File | null = null;
  currentPictureBase64: string = ''; 

  token: string = ''; 

  constructor(private userService: UserService, private authService: AuthService) { }

  ngOnInit(): void {
     const updateStatus = sessionStorage.getItem('updateStatus');

    if (updateStatus) {
      console.log(`Update Status: ${updateStatus}`);  
      switch (updateStatus) {
        case 'success':
          Swal.fire({
            title: 'Success!',
            text: 'Profile updated successfully.',
            icon: 'success',
            confirmButtonText: 'OK'
          });
          break;
        case 'tokenError':
          Swal.fire({
            title: 'Error!',
            text: 'Could not refresh token. Please log in again.',
            icon: 'error',
            confirmButtonText: 'OK'
          }).then(() => {
            window.location.href = '/login';  
          });
          break;
        case 'error':
          Swal.fire({
            title: 'Success!',
            text: 'Profile updated successfully.',
            icon: 'success',
            confirmButtonText: 'OK'
          });
          break;
      }
       sessionStorage.removeItem('updateStatus');
    }

    this.userService.getUserInfo().subscribe(userInfo => {
      this.username = userInfo.username;
      this.email = userInfo.email;

       this.userService.getUserProfilePicture(userInfo.id).subscribe(base64Image => {
        this.currentPictureBase64 = `data:image/jpeg;base64,${base64Image}`;
      });
    });
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.picture = file;
       const reader = new FileReader();
      reader.onload = () => {
        this.currentPictureBase64 = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  updateUser() {
    console.log('Update User called');  
    this.userService.updateUser(this.username, this.email, this.picture, this.token).subscribe(
      (response: any) => {
        console.log('Update response:', response);  
        sessionStorage.setItem('updateStatus', 'success');
         window.location.reload();
      },
      (error) => {
        console.error('Erreur lors de la mise à jour:', error);
        if (error.status === 401) {  
          this.authService.refreshToken(this.token).subscribe(
            (newToken) => {
              this.token = newToken;
              this.updateUser();  
            },
            (refreshError) => {
              console.error('Erreur lors du rafraîchissement du token:', refreshError);  
              sessionStorage.setItem('updateStatus', 'tokenError');
               window.location.reload();
            }
          );
        } else {
           sessionStorage.setItem('updateStatus', 'error');
           window.location.reload();
        }
      }
    );
  }
}
