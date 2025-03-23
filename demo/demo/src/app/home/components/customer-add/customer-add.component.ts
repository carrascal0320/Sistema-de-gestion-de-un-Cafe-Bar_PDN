import { Component, OnInit } from '@angular/core';
import { Customer } from 'src/app/customer';
import { CustomerService } from 'src/app/service/customer.service';

@Component({
  selector: 'app-customer-add',
  templateUrl: './customer-add.component.html',
  styleUrls: ['./customer-add.component.css']
})
export class CustomerAddComponent implements OnInit {
  //Hacer Binding de manera bidireccional con el html y el backend
  firstName: string = '';
  lastName: string = '';
  email: string = '';

  constructor(private customerService : CustomerService){
  }
  
  ngOnInit(): void {
    }
    //Metodo que se asocia con el backend para registrar un nuevo customer
    addCustomer(){
      let customer = new Customer(null, this.firstName, this.lastName, this.email);
      console.log(customer);
      this.customerService.createCustomer(customer).subscribe(
      res => console.log(res)
      );
    }
}
