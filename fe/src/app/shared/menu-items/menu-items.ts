import { Injectable } from '@angular/core'
import { filter } from 'lodash'
import { hasAccess } from '../../helpers/index'

export interface BadgeItem {
  type: string
  value: string
}
export interface Saperator {
  name: string
  type?: string
}
export interface SubChildren {
  state: string
  name: string
  type?: string
}
export interface ChildrenItems {
  state: string
  name: string
  type?: string
  child?: SubChildren[]
}

export interface Menu {
  state: string
  name: string
  type: string
  icon: string
  badge?: BadgeItem[]
  saperator?: Saperator[]
  children?: ChildrenItems[]
}

const MENUITEMS = [
  {
    state: '/school',
    name: 'Student',
    type: 'link',
    icon: 'person',
  },
]

@Injectable()
export class MenuItems {
  getMenuItem(): Menu[] {
    return MENUITEMS
  }
}
