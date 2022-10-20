package pl.tkadziolka.snipmeandroid.ui.preview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.kbiakov.codeview.highlight.ColorThemeData
import io.github.kbiakov.codeview.highlight.SyntaxColors
import kotlinx.android.synthetic.main.fragment_preview.*
import org.koin.android.ext.android.inject
import pl.tkadziolka.snipmeandroid.R
import pl.tkadziolka.snipmeandroid.util.SyntaxTheme
import pl.tkadziolka.snipmeandroid.util.SyntaxWindowTheme
import pl.tkadziolka.snipmeandroid.util.extension.*

private const val HALF = 0.5
private const val ONE_THIRD = 0.3

class PreviewFragment : DialogFragment() {

    private val viewModel: PreviewViewModel by inject()

    private val args: PreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_preview, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showTransparentBackground()
        showFullscreen()

        previewContainer.setOnOutsideClick(R.dimen.spacing_medium) {
            dismiss()
        }

        setupTitle()
        setupCodeView()
        setupActions()
    }

    private fun setupTitle() {
        with(previewTitle) {
            text = args.title
            maxWidth = getMaxWidthPercent(percent = HALF)
        }

        with(previewLanguage) {
            text = args.language
            maxWidth = getMaxWidthPercent(percent = ONE_THIRD)
        }
    }

    private fun setupCodeView() {
        previewCode.setCodeWithTheme(args.code, args.language)
    }

    private fun setupActions() {
        previewTitle.setOnClick { viewModel.goToDetail(findNavController(), args.uuid) }
        previewCopy.setOnClick {
            viewModel.copyToClipboard(args.title, args.code)
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.R) {
                // Android 12+ shows message itself
                showToast(getString(R.string.message_copied, args.title))
            }
        }
    }

    private fun getMaxWidthPercent(percent: Double = HALF): Int {
        val inset = resources.getDimensionPixelSize(R.dimen.spacing_medium)
        val viewWidth = requireActivity().screenWidth
        return ((viewWidth - 2 * inset) * percent).toInt()
    }
}

fun SyntaxTheme.toThemeData(window: SyntaxWindowTheme) = ColorThemeData(
    syntaxColors = SyntaxColors(
        type = keyword,
        keyword = keyword,
        literal = literal,
        comment = comment,
        string = text,
        punctuation = keyword,
        plain = code,
        tag = code,
        attrName = code,
        attrValue = code,
        declaration = code
    ),
    numColor = window.number,
    noteColor = window.note,
    bgNum = window.numberBackground,
    bgContent = window.contentBackground
)