import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageUnitsDialogComponent } from './manage-units-dialog.component';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { UnitTransformationPipe } from '../../shared/pipes/unit-transformation/unit-transformation.pipe';
import { DialogTemplateCoreComponent } from '../../shared/custom/dialog-template-core/dialog-template-core.component';
import { ErrorComponent } from '../../shared/custom/error/error.component';
import { ActionPlanComponent, initFormGroupFromItem, Item } from '../action-plan/action-plan.component';
import { FormArray, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { UnitService } from '../../services/unit.service';
import { Unit } from '../../shared/types/enums/unit';
import { testUser, UNIT_CHF, UNIT_NUMBER } from '../../shared/test-data';
import { of } from 'rxjs';

const units: Unit[] = [
  { id: 1,
    unitName: 'Kilogram',
    owner: testUser,
    isDefault: false },
  { id: 1,
    unitName: 'Kilogram',
    owner: testUser,
    isDefault: false },
  { id: undefined,
    unitName: 'Meter',
    owner: testUser,
    isDefault: false },
  { id: undefined,
    unitName: 'Meter',
    owner: testUser,
    isDefault: false },
  { id: undefined,
    unitName: 'Meter',
    owner: testUser,
    isDefault: false }
];

describe('ManageUnitsDialogComponent', () => {
  let component: ManageUnitsDialogComponent;
  let fixture: ComponentFixture<ManageUnitsDialogComponent>;
  const unitServiceMock = {
    getUnits: jest.fn()
      .mockReturnValue(of([])),
    getAllFromUser: jest.fn()
      .mockReturnValue(of([])),
    updateUnit: jest.fn()
      .mockReturnValue(of({})),
    createUnit: jest.fn()
      .mockReturnValue(of({}))
  };

  const dialogRefMock = {
    close: jest.fn()
  };

  const unitPipeMock = {
    transformLabel: jest.fn((val) => val) // Default mock simply returns input
  };
  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [
        ManageUnitsDialogComponent,
        DialogTemplateCoreComponent,
        ErrorComponent,
        ActionPlanComponent
      ],
      imports: [
        TranslateModule.forRoot(),
        MatDialogModule,
        ReactiveFormsModule,
        MatIconModule,
        MatDividerModule
      ],
      providers: [
        UnitTransformationPipe,
        TranslateService,
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: MatDialogRef,
          useValue: {}
        },
        {
          provide: UnitService,
          useValue: unitServiceMock
        },
        {
          provide: MatDialogRef,
          useValue: dialogRefMock
        },
        {
          provide: UnitTransformationPipe,
          useValue: unitPipeMock
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ManageUnitsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should push units from getAllFromUser into actinPlanAddItemSubject', () => {
    // Arrange
    const mockUnits = [UNIT_NUMBER,
      UNIT_CHF] as Unit[];
    unitServiceMock.getAllFromUser.mockReturnValue(of(mockUnits));


    // Act
    component.ngOnInit();

    // Assert
    expect((component.fg.get('unitFormArray') as FormArray)?.length)
      .toBe(2);
  });

  it('should filter and return only units with an id', () => {
    const result = component.getUpdatableUnits(units);

    // Assert
    expect(result.length)
      .toBe(2);
  });

  it('should filter and return only units without an id', () => {
    const result = component.getNewUnits(units);

    // Assert
    expect(result.length)
      .toBe(3);
  });

  it('should call updateUnit for units with an id and createUnit for units without an id, then close dialog', () => {
    const mockItems: Item[] = [
      { id: 1,
        item: 'Kilogram',
        version: 1,
        isChecked: false },
      { id: 1,
        item: 'Kilogram',
        version: 1,

        isChecked: false },
      { id: undefined,
        item: 'Meter',
        version: 1,

        isChecked: false },
      { id: undefined,
        item: 'Meter',
        version: 1,

        isChecked: false },
      { id: undefined,
        item: 'Meter',
        version: 1,

        isChecked: false }
    ];
    (component.fg.get('unitFormArray') as FormArray)?.clear();
    mockItems.forEach((item) => initFormGroupFromItem(item));

    jest.spyOn(component, 'getChangedItems')
      .mockReturnValue(mockItems);

    const updateUnitSpy = jest.spyOn(unitServiceMock, 'updateUnit');
    updateUnitSpy.mockReset();
    const createUnitSpy = jest.spyOn(unitServiceMock, 'createUnit');
    createUnitSpy.mockReset();

    const closeSpy = jest.spyOn(dialogRefMock, 'close');

    unitServiceMock.updateUnit.mockReturnValue(of({}));
    unitServiceMock.createUnit.mockReturnValue(of({}));

    component.submit();

    expect(updateUnitSpy)
      .toHaveBeenCalledTimes(2);
    expect(createUnitSpy)
      .toHaveBeenCalledTimes(3);
    expect(closeSpy)
      .toHaveBeenCalled();
  });

  describe('getFormatedUnitSymbol', () => {
    it('should return an empty string if transformLabel returns the same value or empty', () => {
      // Arrange
      const mockUnit = { unitName: 'Kilogram' } as Unit;
      unitPipeMock.transformLabel.mockReturnValue('Kilogram');

      // Act
      const result = component.getFormatedUnitSymbol(mockUnit);

      // Assert
      expect(result)
        .toBe('');
      expect(unitPipeMock.transformLabel)
        .toHaveBeenCalledWith('Kilogram');
    });

    it('should return the formatted string in parentheses if transformLabel differs', () => {
      // Arrange
      const mockUnit = { unitName: 'Kilogram' } as Unit;
      unitPipeMock.transformLabel.mockReturnValue('kg');

      // Act
      const result = component.getFormatedUnitSymbol(mockUnit);

      // Assert
      expect(result)
        .toBe('(kg)');
      expect(unitPipeMock.transformLabel)
        .toHaveBeenCalledWith('Kilogram');
    });
  });
});


