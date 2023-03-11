import { Component, OnInit, ViewChild, TemplateRef, HostListener, ElementRef, Input, Output, EventEmitter } from '@angular/core';

import { FormGroup, FormArray, FormControl, ValidatorFn, ValidationErrors } from "@angular/forms";
import {FormBuilder, Validators} from '@angular/forms';
/**
 * @title Basic use of the tab group
 */
@Component({
  selector: 'app-vertical-tabs',
  templateUrl: 'vertical_tabs.component.html',
  styleUrls: ['vertical_tabs.component.css'],
})
export class VerticalTabComponent {
  constructor(
    private fb: FormBuilder,
  ) {
  }
  ruleScoreOption = [
    {
      "value":"1",
      "label":"Internal"
    },
    {
      "value":"2",
      "label":"External"
    },    
    {
      "value":"3",
      "label":"Credit"
    },    
    {
      "value":"4",
      "label":"Deposit"
    },  
    {
      "value":"5",
      "label":"Random"
    }
  ]
  
  selectedIndex = 0;
  @Input() formData: any ;
  @Input() ruleFormData: any ;
  @Output() updatedData = new EventEmitter();
  buttonsEvent(data){
    this.updatedData.emit(data);
  }
  ngOnChanges() {
    this.loadRuleFormData(this.ruleFormData);
  }
  loadRuleFormData(formData){
    for(let i = 0; i < formData.length; i++){     
        if(this.ruleScores.length < formData.length){
          this.addruleScores(formData[i]);
        }   
    }
  }

  form = this.fb.group({
    ruleScores: this.fb.array([])
  });


  get ruleScores() {
    return this.form.controls["ruleScores"] as FormArray;
  }

  addruleScores(fieldName:any) {
    const ruleScoresForm = this.fb.group({
      id:'',
      ruleFieldName: fieldName,
      fieldName: '',
      ruleScoresOption: '',
      // ruleScores: this.fb.array([])
    });
    this.ruleScores.push(ruleScoresForm);
  }

  deleteruleScores(ruleScoresIndex: number) {
    this.ruleScores.removeAt(ruleScoresIndex);
  }
  
  @Output() returnResult = new EventEmitter();
  radioChange($event,index){
    this.form.controls['ruleScores']['controls'][index].controls['id'].setValue(index);
    this.form.controls['ruleScores']['controls'][index].controls['ruleScoresOption'].setValue($event.value);
    this.returnResult.emit(this.form.controls['ruleScores']['controls']);
  }
  cancelChange($event,index){
    this.form.controls['ruleScores']['controls'][index].controls['ruleScoresOption'].setValue("");
    this.returnResult.emit(this.form.controls['ruleScores']['controls']);
  }
  onTabChanged($event,index) {
    let clickedIndex = $event.index;
    if(this.form.controls['ruleScores']['controls'][index].controls['fieldName'].value != this.formData[clickedIndex]){
      this.form.controls['ruleScores']['controls'][index].controls['ruleScoresOption'].setValue("");
    }
    this.form.controls['ruleScores']['controls'][index].controls['fieldName'].setValue(this.formData[clickedIndex]);
    this.returnResult.emit(this.form.controls['ruleScores']['controls']);
  }
}

/**  Copyright 2019 Google LLC. All Rights Reserved.
    Use of this source code is governed by an MIT-style license that
    can be found in the LICENSE file at http://angular.io/license */