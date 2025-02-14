import { EventData } from "tns-core-modules/data/observable";
import { ObservableArray } from "tns-core-modules/data/observable-array";
import { View, KeyedTemplate } from "tns-core-modules/ui/core/view";
import { Page } from "tns-core-modules/ui/page";
import { ViewModel, Item } from "./main-view-model";
import { ListView } from "tns-core-modules/ui/list-view";
import { Label } from "tns-core-modules/ui/label";
import { GridLayout } from "tns-core-modules/ui/layouts/grid-layout";
import { Color } from "tns-core-modules/color";

export function selectItemTemplate(item: Item, index: number, items: ObservableArray<Item>): string {
    return item.id % 10 === 0 ? "red" : item.id % 2 === 0 ? "green" : "yellow";
}

export function pageLoaded(args: EventData) {
  let page = <Page>args.object;
  page.bindingContext = new ViewModel();

  let lv4 = page.getViewById<ListView>("lv4");
  lv4.itemTemplateSelector = (item: Item, index: number, items: ObservableArray<Item>) => {
    return index % 10 === 0 ? "red" : index % 2 === 0 ? "green" : "yellow";
  };

  let createLabel = (backgroundColor: Color) => {
    let label = new Label();
    label.bind({
        sourceProperty: null,
        targetProperty: "text",
        expression: "$value"
    });
    label.style.backgroundColor = backgroundColor;

    return label;
  };

  lv4.itemTemplates = new Array<KeyedTemplate>(
      {
          key: "red",
          createView: () => createLabel(new Color("red"))
      },
      {
          key: "green",
          createView: () => createLabel(new Color("green"))
      },
      {
          key: "yellow",
          createView: () => createLabel(new Color("yellow"))
      }
  );
}

let scrollToBottom = true;
export function onScroll(args: EventData) {
  let page = (<View>args.object).page;
  let gridLayout = page.getViewById<GridLayout>("grid-layout");
  for (let i = 0, length = gridLayout.getChildrenCount(); i < length; i++) {
      let listView = <ListView>gridLayout.getChildAt(i);
      listView.scrollToIndex(scrollToBottom ? listView.items.length - 1 : 0);
  }
  scrollToBottom = !scrollToBottom;
}

export function onScrollToIndex(args: EventData) {
    let page = (<View>args.object).page;
    let gridLayout = page.getViewById<GridLayout>("grid-layout");
    for (let i = 0, length = gridLayout.getChildrenCount(); i < length; i++) {
        let listView = <ListView>gridLayout.getChildAt(i);
        listView.scrollToIndex(50);
    }
}

export function onScrollToIndexAnimated(args: EventData) {
    let page = (<View>args.object).page;
    let gridLayout = page.getViewById<GridLayout>("grid-layout");
    for (let i = 0, length = gridLayout.getChildrenCount(); i < length; i++) {
        let listView = <ListView>gridLayout.getChildAt(i);
        listView.scrollToIndexAnimated(50);
    }
}

export function onScrollReset(args: EventData) {
    let page = (<View>args.object).page;
    let gridLayout = page.getViewById<GridLayout>("grid-layout");
    for (let i = 0, length = gridLayout.getChildrenCount(); i < length; i++) {
        let listView = <ListView>gridLayout.getChildAt(i);
        listView.scrollToIndex(0);
    }
}