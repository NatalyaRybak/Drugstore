import {Injectable} from "@angular/core";
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from "@angular/common/http";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import "rxjs-compat/add/operator/do";
import {AuthenticationService} from "../services/authentication.service";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(public auth: AuthenticationService, private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.getToken();
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(request).do(
      (event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          // do stuff with response if you want
        }
      }, (err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {
            this.auth.logout();
            // this.auth.removeCurrUser();
            this.router.navigateByUrl("/signin");
          }
          else if(err.status === 403){
            window.alert(err.error || "You are not allowed to enter this page.");
          }
          else if(err.status === 500){
            window.alert(err.error.error || err.error || "Internal server error");
          }
        }
      });
  }
}
