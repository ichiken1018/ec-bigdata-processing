"use strict";
$(function () {
  //必ず実行
  // 親カテゴリが変更されたら実行
  $("#parent-id").on("change", function () {
    const parentId = $("#parent-id").val();
	//console.log("parentId" + parentId);
    if (parentId == -1) {
      $("#child-id").hide();
      $("#grand-child-id").hide();
      return;
    }

    $.ajax({
      url: "http://localhost:8080/ec-bigdata/pick-up-category-list/child-category",
      type: "GET",
      dataType: "JSON",
      data: {
        parentId: parentId,
      },

      async: true,
    })
      .done(function (data) {
        //成功
        const childCategoryList = data.childCategoryList;
        
        if (childCategoryList.length == 0) {
          $("#child-id").hide();
          $("#grand-child-id").hide();
          return;
        }

        //元データを削除
        $("#child-id").children().remove();
        $("#child-id").append(
          $("<option value='-1'>").text("- childCategory -")
        );
        //子カテゴリを挿入
        for (let i = 0; i < childCategoryList.length; i++) {
          $("#child-id").append(
            $("<option>")
              .val(childCategoryList[i].id)
              .text(childCategoryList[i].name)
          );
        }
        //子カテゴリを表示
        $("#child-id").show();
        //孫カテゴリを非表示
        $("#grand-child-id").hide();
      })
      .fail(function (XMLHttpRequest, textStatus, errorThrown) {
        // 検索失敗時には、その旨をダイアログ表示

        console.log("XMLHttpRequest : " + XMLHttpRequest.status);
        console.log("textStatus     : " + textStatus);
        console.log("errorThrown    : " + errorThrown.message);
      });
  });

  // 子カテゴリが変更されたら実行
  $("#child-id").on("change", function () {
    const childId = $("#child-id").val();

    if (childId == -1) {
      $("#grand-child-id").hide();
      return;
    }

    $.ajax({
      url: "http://localhost:8080/ec-bigdata/pick-up-category-list/grand-child-category",
      type: "GET",
      dataType: "JSON",
      data: {
        childId: childId,
      },

      async: true,
    })
      .done(function (data) {
        //成功
        const grandChildCategoryList = data.grandChildCategoryList;
        //削除
        $("#grand-child-id").children().remove();
        //デフォルトを挿入
        $("#grand-child-id").append(
          $("<option value='-1'>").text("- grandChild -")
        );
        //孫カテゴリを挿入
        for (let i = 0; i < grandChildCategoryList.length; i++) {
          $("#grand-child-id").append(
            $("<option>")
              .val(grandChildCategoryList[i].id)
              .text(grandChildCategoryList[i].name)
          );
        }
        $("#grand-child-id").show();
      })
      .fail(function (XMLHttpRequest, textStatus, errorThrown) {
        // 検索失敗時には、その旨をダイアログ表示

        console.log("XMLHttpRequest : " + XMLHttpRequest.status);
        console.log("textStatus     : " + textStatus);
        console.log("errorThrown    : " + errorThrown.message);
      });
  });
});