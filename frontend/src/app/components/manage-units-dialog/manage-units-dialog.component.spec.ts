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
import { ActionPlanComponent, FormControlsOf, Item } from '../action-plan/action-plan.component';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { UnitService } from '../../services/unit.service';
import { Unit } from '../../shared/types/enums/unit';
import { testUser, UNIT_CHF, UNIT_NUMBER } from '../../shared/test-data';
import { of } from 'rxjs';

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

    const subjectNextSpy = jest.spyOn(component.actinPlanAddItemSubject, 'next');

    // Act
    component.ngOnInit();

    // Assert
    expect(subjectNextSpy)
      .toHaveBeenCalledTimes(mockUnits.length);
    mockUnits.forEach((unit, i) => {
      expect(subjectNextSpy)
        .toHaveBeenNthCalledWith(i + 1, expect.objectContaining({ id: unit.id,
          item: unit.unitName }));
    });
  });

  it('should filter and return only units with an id', () => {
    // Arrange
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

    // Act
    const result = component.getUpdatableUnits(units);

    // Assert
    expect(result.length)
      .toBe(2);
  });

  it('should filter and return only units without an id', () => {
    // Arrange
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
    // Act
    const result = component.getNewUnits(units);

    // Assert
    expect(result.length)
      .toBe(3);
  });

  it('should call updateUnit for units with an id and createUnit for units without an id, then close dialog', () => {
    const mockItems: Item[] = [
      { id: 1,
        item: 'Kilogram',
        isChecked: false },
      { id: 1,
        item: 'Kilogram',
        isChecked: false },
      { id: undefined,
        item: 'Meter',
        isChecked: false },
      { id: undefined,
        item: 'Meter',
        isChecked: false },
      { id: undefined,
        item: 'Meter',
        isChecked: false }
    ];

    mockItems.forEach((item) => addNewItem(item));

    const updateUnitSpy = jest.spyOn(unitServiceMock, 'updateUnit');
    const createUnitSpy = jest.spyOn(unitServiceMock, 'createUnit');
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
  function addNewItem(item?: Item) {
    const newFormGroup = new FormGroup({
      item: new FormControl<string>(item?.item || ''),
      id: new FormControl<number | undefined>(item?.id || undefined),
      isChecked: new FormControl<boolean>(item?.isChecked || false)
    } as FormControlsOf<Item>);
    (component.fg.get('unitFormArray') as FormArray)?.push(newFormGroup);
  }
});


