import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../models/user.model';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  loading = true;
  error: string | null = null;
  
  // Para edición y creación de usuarios
  selectedUser: User | null = null;
  isEditing = false;
  newUser: User = {
    id: 0,
    email: '',
    password: '',
    name: '',
    phone: null
  };

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar usuarios: ' + err.message;
        this.loading = false;
      }
    });
  }

  deleteUser(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.users = this.users.filter(user => user.id !== id);
        },
        error: (err) => {
          this.error = 'Error al eliminar usuario: ' + err.message;
        }
      });
    }
  }

  editUser(user: User): void {
    this.selectedUser = { ...user };
    this.isEditing = true;
  }

  cancelEdit(): void {
    this.selectedUser = null;
    this.isEditing = false;
  }

  saveUser(): void {
    if (this.selectedUser) {
      this.userService.updateUser(this.selectedUser.id, this.selectedUser).subscribe({
        next: (updatedUser) => {
          const index = this.users.findIndex(u => u.id === updatedUser.id);
          if (index !== -1) {
            this.users[index] = updatedUser;
          }
          this.cancelEdit();
        },
        error: (err) => {
          this.error = 'Error al actualizar usuario: ' + err.message;
        }
      });
    }
  }

  showAddForm(): void {
    this.newUser = {
      id: 0,
      email: '',
      password: '',
      name: '',
      phone: null
    };
    this.isEditing = false;
    this.selectedUser = this.newUser;
  }

  addUser(): void {
    this.userService.createUser(this.newUser).subscribe({
      next: (createdUser) => {
        this.users.push(createdUser);
        this.cancelEdit();
      },
      error: (err) => {
        this.error = 'Error al crear usuario: ' + err.message;
      }
    });
  }
}