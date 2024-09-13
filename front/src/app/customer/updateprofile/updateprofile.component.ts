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
  currentPictureBase64: string = ''; // Image actuelle en Base64

  token: string = ''; // Ton token JWT

  constructor(private userService: UserService, private authService: AuthService) { }

  ngOnInit(): void {
    // Vérifier l'état temporaire après le rechargement de la page
    const updateStatus = sessionStorage.getItem('updateStatus');

    if (updateStatus) {
      console.log(`Update Status: ${updateStatus}`); // Debugging line
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
            window.location.href = '/login'; // Rediriger vers la page de connexion
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
      // Nettoyer l'état temporaire après affichage de l'alerte
      sessionStorage.removeItem('updateStatus');
    }

    this.userService.getUserInfo().subscribe(userInfo => {
      this.username = userInfo.username;
      this.email = userInfo.email;

      // Récupérer la photo de profil au format Base64
      this.userService.getUserProfilePicture(userInfo.id).subscribe(base64Image => {
        this.currentPictureBase64 = `data:image/jpeg;base64,${base64Image}`;
      });
    });
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.picture = file;
      // Mettre à jour l'aperçu de l'image
      const reader = new FileReader();
      reader.onload = () => {
        this.currentPictureBase64 = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  updateUser() {
    console.log('Update User called'); // Debugging line
    this.userService.updateUser(this.username, this.email, this.picture, this.token).subscribe(
      (response: any) => {
        console.log('Update response:', response); // Debugging line
        // Stocker l'état temporaire pour l'affichage ultérieur
        sessionStorage.setItem('updateStatus', 'success');
        // Rafraîchir la page
        window.location.reload();
      },
      (error) => {
        console.error('Erreur lors de la mise à jour:', error);
        if (error.status === 401) { // Token expiré
          this.authService.refreshToken(this.token).subscribe(
            (newToken) => {
              this.token = newToken;
              this.updateUser(); // Réessayer la mise à jour avec le nouveau token
            },
            (refreshError) => {
              console.error('Erreur lors du rafraîchissement du token:', refreshError); // Debugging line
              // Stocker l'état temporaire pour l'affichage ultérieur
              sessionStorage.setItem('updateStatus', 'tokenError');
              // Rafraîchir la page
              window.location.reload();
            }
          );
        } else {
          // Stocker l'état temporaire pour l'affichage ultérieur
          sessionStorage.setItem('updateStatus', 'error');
          // Rafraîchir la page
          window.location.reload();
        }
      }
    );
  }
}
