package lampa.test.tmdblib.view.fragments.dialog.callback

interface CallBackFromDialogToFragment {

    fun createCodeWithNumberPhone(number: String)
    fun authWithCode(code: String)
}