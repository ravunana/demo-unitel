import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { FacturaService } from 'app/entities/facturacao/factura/factura.service';
import { IFactura, Factura } from 'app/shared/model/facturacao/factura.model';
import { Tipo } from 'app/shared/model/enumerations/tipo.model';

describe('Service Tests', () => {
  describe('Factura Service', () => {
    let injector: TestBed;
    let service: FacturaService;
    let httpMock: HttpTestingController;
    let elemDefault: IFactura;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(FacturaService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Factura(0, Tipo.COMPRA, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            data: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Factura', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            data: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            data: currentDate
          },
          returnedFromService
        );
        service
          .create(new Factura(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Factura', () => {
        const returnedFromService = Object.assign(
          {
            tipo: 'BBBBBB',
            beneficiario: 'BBBBBB',
            data: currentDate.format(DATE_TIME_FORMAT),
            numero: 'BBBBBB',
            produtoCodigo: 'BBBBBB',
            produtoNome: 'BBBBBB',
            produtoPreco: 1,
            produtoQuantidade: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            data: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Factura', () => {
        const returnedFromService = Object.assign(
          {
            tipo: 'BBBBBB',
            beneficiario: 'BBBBBB',
            data: currentDate.format(DATE_TIME_FORMAT),
            numero: 'BBBBBB',
            produtoCodigo: 'BBBBBB',
            produtoNome: 'BBBBBB',
            produtoPreco: 1,
            produtoQuantidade: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            data: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Factura', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
