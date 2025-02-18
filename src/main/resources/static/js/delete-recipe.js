function deleteRecipe(id) {
    if (confirm("本当に削除しますか？")) {
        fetch(`/recipes/${id}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (response.ok) {
                alert("レシピが削除されました。");
                window.location.href = '/recipes';  // 削除後、レシピ一覧にリダイレクト
            } else {
                alert("削除に失敗しました。");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("削除中にエラーが発生しました。");
        });
    }
}
