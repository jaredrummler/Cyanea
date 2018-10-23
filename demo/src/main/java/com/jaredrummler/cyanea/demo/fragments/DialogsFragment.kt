package com.jaredrummler.cyanea.demo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.jaredrummler.cyanea.app.CyaneaFragment
import com.jaredrummler.cyanea.demo.R

class DialogsFragment : CyaneaFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    val view = inflater.inflate(R.layout.fragment_dialogs, container, false)

    val dialogLaunchersLayout = view.findViewById<ViewGroup>(R.id.dialog_launcher_buttons_layout)
    val choices = arrayOf<CharSequence>("Choice1", "Choice2", "Choice3")
    val choicesInitial = booleanArrayOf(false, true, false)
    val multiLineMessage = StringBuilder()
    val line = resources.getString(R.string.line)
    for (i in 0..99) {
      multiLineMessage.append(line).append(i).append("\n")
    }
    val positiveText = resources.getString(R.string.positive)
    val negativeText = resources.getString(R.string.negative)
    val neutralText = resources.getString(R.string.neutral)
    val title = resources.getString(R.string.title)
    val message = resources.getString(R.string.message)

    // message, 2 actions
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.message_2_actions,
        AlertDialog.Builder(requireActivity())
            .setMessage(message)
            .setPositiveButton(positiveText, null)
            .setNegativeButton(negativeText, null))

    // title, 2 actions
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_2_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setPositiveButton(positiveText, null)
            .setNeutralButton(neutralText, null))

    // title, message, 3 actions (short)
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_message_3_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, null)
            .setNegativeButton(negativeText, null)
            .setNeutralButton(neutralText, null))

    // title, message, 3 actions (long)
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_message_3_long_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.long_positive), null)
            .setNegativeButton(resources.getString(R.string.long_negative), null)
            .setNeutralButton(resources.getString(R.string.long_neutral), null))

    // long title, message, 1 action (too long)
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.long_title_message_too_long_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(resources.getString(R.string.long_title))
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.too_long_positive), null)
            .setNegativeButton(resources.getString(R.string.too_long_negative), null)
            .setNeutralButton(resources.getString(R.string.too_long_neutral), null))

    // icon, title, message, 2 actions
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.icon_title_message_2_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, null)
            .setNegativeButton(negativeText, null)
            .setIcon(R.drawable.ic_dialogs_24px))

    // title, auto-action choice dialog
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_choices_as_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setNeutralButton(neutralText, null)
            .setItems(choices, null))

    // title, checkboxes, 2 actions dialog
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_checkboxes_2_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setPositiveButton(positiveText, null)
            .setNeutralButton(neutralText, null)
            .setMultiChoiceItems(choices, choicesInitial, null))

    // title, radiobutton, 2 actions dialog
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_radiobuttons_2_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setPositiveButton(positiveText, null)
            .setNeutralButton(neutralText, null)
            .setSingleChoiceItems(choices, 1, null))

    // title, custom view, actions dialog
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_slider_2_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setPositiveButton(positiveText, null)
            .setNeutralButton(neutralText, null)
            .setView(SeekBar(requireActivity())))

    // title, scrolling long view, actions dialog
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_scrolling_2_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(multiLineMessage.toString())
            .setPositiveButton(positiveText, null)
            .setNeutralButton(neutralText, null))

    // title, short buttons
    addDialogLauncher(
        dialogLaunchersLayout,
        R.string.title_2_short_actions,
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setPositiveButton(R.string.short_text_1, null)
            .setNeutralButton(R.string.short_text_2, null))

    return view
  }

  @SuppressLint("RestrictedApi")
  private fun addDialogLauncher(
      viewGroup: ViewGroup, @StringRes stringResId: Int, alertDialogBuilder: AlertDialog.Builder) {
    val dialogLauncherButton = MaterialButton(viewGroup.context)
    dialogLauncherButton.setOnClickListener { alertDialogBuilder.show() }
    dialogLauncherButton.setText(stringResId)
    viewGroup.addView(dialogLauncherButton)
  }

}