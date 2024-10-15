package a.a.a;

import static mod.Edward.KOC.WidgetCreator.IconCustomWidget.AddExtraClasses;
import static mod.Edward.KOC.WidgetCreator.IconCustomWidget.AddWidgetsByTitle;
import static mod.Edward.KOC.WidgetCreator.IconCustomWidget.GetWidgetsListMap;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.besome.sketch.beans.HistoryViewBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.design.DesignActivity;
import com.besome.sketch.editor.LogicEditorActivity;
import com.besome.sketch.editor.PropertyActivity;
import com.besome.sketch.editor.view.DraggingListener;
import com.besome.sketch.editor.view.ViewEditor;
import com.besome.sketch.editor.view.ViewProperty;
import com.besome.sketch.editor.view.palette.PaletteWidget;
import com.sketchware.remod.R;

import java.util.ArrayList;

public class ViewEditorFragment extends qA {

    private ProjectFileBean projectFileBean;
    private static ViewEditor viewEditor;
    private boolean isFabEnabled = false;
    private ViewProperty viewProperty;
    private ObjectAnimator n;
    private ObjectAnimator o;
    private boolean p;
    private boolean q = false;
    private String sc_id;

    public ViewEditorFragment() {
    }

    private void initialize(ViewGroup viewGroup) {
        setHasOptionsMenu(true);
        GetWidgetsListMap();
        viewEditor = viewGroup.findViewById(R.id.view_editor);
        viewEditor.setScreenType(getResources().getConfiguration().orientation);
        viewProperty = requireActivity().findViewById(R.id.view_property);
        viewProperty.setOnPropertyListener(new Iw() {
            @Override
            public void a() {
                viewEditor.setFavoriteData(Rp.h().f());
            }

            @Override
            public void a(String s, ViewBean viewBean) {
                openPropertyActivity(viewBean);
            }
        });
        viewProperty.setOnPropertyValueChangedListener(viewBean -> {
            a(viewBean.id);
            viewProperty.e();
            invalidateOptionsMenu();
        });
        viewProperty.setOnEventClickListener(eventBean -> toLogicEditorActivity(eventBean.targetId, eventBean.eventName, eventBean.eventName));
        viewProperty.setOnPropertyTargetChangeListener(viewEditor::updateSelection);
        viewEditor.setOnWidgetSelectedListener(new cy() {
            @Override
            public void a() {
                n();
                viewProperty.e();
            }

            @Override
            public void a(String viewId) {
                n();
                viewProperty.a(viewId);
            }

            @Override
            public void a(boolean var1, String viewId) {
                if (!viewId.isEmpty()) {
                    a();
                    viewProperty.a(viewId);
                    viewProperty.e();
                }

                ViewEditorFragment.this.a(var1);
            }
        });
        viewEditor.setOnDraggingListener(new DraggingListener() {
            @Override
            public boolean isAdmobEnabled() {
                return jC.c(sc_id).b().isEnabled();
            }

            @Override
            public void b() {
                q = true;
                ((DesignActivity) requireActivity()).setTouchEventEnabled(false);
            }

            @Override
            public boolean isGoogleMapEnabled() {
                return jC.c(sc_id).e().isEnabled();
            }

            @Override
            public void d() {
                q = false;
                ((DesignActivity) requireActivity()).setTouchEventEnabled(true);
            }
        });
        viewEditor.setOnHistoryChangeListener(this::invalidateOptionsMenu);
        viewEditor.setFavoriteData(Rp.h().f());
    }

    public void a(ProjectFileBean projectFileBean) {
        this.projectFileBean = projectFileBean;
        isFabEnabled = projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FAB);
        viewEditor.a(sc_id, projectFileBean);
        viewEditor.h();
        viewProperty.a(sc_id, this.projectFileBean);
        e();
        i();
        invalidateOptionsMenu();
    }

    private void a(ViewBean viewBean) {
        viewEditor.removeFab();
        if (isFabEnabled) viewEditor.addFab(viewBean);
    }

    private void a(String viewId) {
        ViewBean viewBean;
        if (viewId.equals("_fab")) {
            viewBean = jC.a(sc_id).h(projectFileBean.getXmlName());
        } else {
            viewBean = jC.a(sc_id).c(projectFileBean.getXmlName(), viewId);
        }
        c(viewBean);
        viewProperty.e();
    }

    private void toLogicEditorActivity(String eventId, String eventName, String eventName2) {
        Intent intent = new Intent(requireContext(), LogicEditorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("id", eventId);
        intent.putExtra("event", eventName);
        intent.putExtra("project_file", projectFileBean);
        intent.putExtra("event_text", eventName2);
        requireContext().startActivity(intent);
    }

    public void a(ArrayList<ViewBean> viewBeans) {
        viewEditor.h();
        viewEditor.a(eC.a(viewBeans));
    }

    public void a(boolean var1) {
        startAnimation();
        if (!p || !var1) {
            cancelAnimations();
            if (var1) {
                n.start();
            } else if (p) {
                o.start();
            }

            p = var1;
        }
    }

    public void openPropertyActivity(ViewBean viewBean) {
        Intent intent = new Intent(requireContext(), PropertyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("bean", viewBean);
        intent.putExtra("project_file", projectFileBean);
        startActivityForResult(intent, 213);
    }

    private void b(ArrayList<ViewBean> viewBeans) {
        l();
        a(viewBeans);
    }

    public void b(boolean var1) {
        viewEditor.setIsAdLoaded(var1);
        viewEditor.requestLayout();
    }

    private void cancelAnimations() {
        if (n.isRunning()) n.cancel();
        if (o.isRunning()) o.cancel();
    }

    public void c(ViewBean var1) {
        viewEditor.e(var1);
    }

    public void c(boolean var1) {
        viewProperty.setVisibility(var1 ? View.VISIBLE : View.GONE);
    }

    public ProjectFileBean d() {
        return projectFileBean;
    }

    public static void e() {
        viewEditor.removeWidgetsAndLayouts();
        viewEditor.setPaletteLayoutVisible(View.VISIBLE);
        viewEditor.addWidgetLayout(PaletteWidget.a.a, "");
        viewEditor.addWidgetLayout(PaletteWidget.a.b, "");
        viewEditor.addWidget(PaletteWidget.b.b, "", "TextView", "TextView");
        viewEditor.addWidgetLayout(PaletteWidget.a.c, "");
        viewEditor.addWidgetLayout(PaletteWidget.a.d, "");
        viewEditor.extraWidgetLayout("", "RadioGroup");
        AddWidgetsByTitle(viewEditor, "Layouts");

        viewEditor.paletteWidget.extraTitle("AndroidX", 0);
        viewEditor.extraWidgetLayout("", "TabLayout");
        viewEditor.extraWidgetLayout("", "BottomNavigationView");
        viewEditor.extraWidgetLayout("", "CollapsingToolbarLayout");
        viewEditor.extraWidgetLayout("", "CardView");
        viewEditor.extraWidgetLayout("", "TextInputLayout");
        viewEditor.extraWidgetLayout("", "SwipeRefreshLayout");
        AddWidgetsByTitle(viewEditor, "AndroidX");

        viewEditor.addWidget(PaletteWidget.b.c, "", "EditText", "Edit Text");
        viewEditor.extraWidget("", "AutoCompleteTextView", "AutoCompleteTextView");
        viewEditor.extraWidget("", "MultiAutoCompleteTextView", "MultiAutoCompleteTextView");
        viewEditor.addWidget(PaletteWidget.b.a, "", "Button", "Button");
        viewEditor.extraWidget("", "MaterialButton", "MaterialButton");
        viewEditor.addWidget(PaletteWidget.b.d, "", "ImageView", "default_image");
        viewEditor.extraWidget("", "CircleImageView", "default_image");
        viewEditor.addWidget(PaletteWidget.b.g, "", "CheckBox", "CheckBox");
        viewEditor.extraWidget("", "RadioButton", "RadioButton");
        viewEditor.addWidget(PaletteWidget.b.i, "", "Switch", "Switch");
        viewEditor.addWidget(PaletteWidget.b.j, "", "SeekBar", "SeekBar");
        viewEditor.addWidget(PaletteWidget.b.m, "", "ProgressBar", "ProgressBar");
        viewEditor.extraWidget("", "RatingBar", "RatingBar");
        viewEditor.extraWidget("", "SearchView", "SearchView");
        viewEditor.extraWidget("", "VideoView", "VideoView");
        viewEditor.addWidget(PaletteWidget.b.h, "", "WebView", "WebView");
        AddWidgetsByTitle(viewEditor, "Widgets");

        viewEditor.paletteWidget.extraTitle("List", 1);
        viewEditor.addWidget(PaletteWidget.b.e, "", "ListView", "ListView");
        viewEditor.extraWidget("", "GridView", "GridView");
        viewEditor.extraWidget("", "RecyclerView", "RecyclerView");
        viewEditor.addWidget(PaletteWidget.b.f, "", "Spinner", "Spinner");
        viewEditor.extraWidget("", "ViewPager", "ViewPager");
        AddWidgetsByTitle(viewEditor, "List");

        viewEditor.paletteWidget.extraTitle("Library", 1);
        viewEditor.extraWidget("", "WaveSideBar", "WaveSideBar");
        viewEditor.extraWidget("", "PatternLockView", "PatternLockView");
        viewEditor.extraWidget("", "CodeView", "CodeView");
        viewEditor.extraWidget("", "LottieAnimation", "LottieAnimation");
        viewEditor.extraWidget("", "OTPView", "OTPView");
        AddWidgetsByTitle(viewEditor, "Library");

        viewEditor.paletteWidget.extraTitle("Google", 1);
        viewEditor.addWidget(PaletteWidget.b.l, "", "AdView", "AdView");
        viewEditor.addWidget(PaletteWidget.b.n, "", "MapView", "MapView");
        viewEditor.extraWidget("", "SignInButton", "SignInButton");
        viewEditor.extraWidget("", "YoutubePlayer", "YoutubePlayer");
        AddWidgetsByTitle(viewEditor, "Google");

        viewEditor.paletteWidget.extraTitle("Date & Time", 1);
        viewEditor.extraWidget("", "AnalogClock", "AnalogClock");
        viewEditor.extraWidget("", "DigitalClock", "DigitalClock");
        viewEditor.extraWidget("", "TimePicker", "TimePicker");
        viewEditor.extraWidget("", "DatePicker", "DatePicker");
        viewEditor.addWidget(PaletteWidget.b.k, "", "CalendarView", "CalendarView");
        AddWidgetsByTitle(viewEditor, "Date & Time");
        AddExtraClasses(viewEditor);
    }

    private void startAnimation() {
        if (n == null) {
            n = ObjectAnimator.ofFloat(viewProperty, View.TRANSLATION_Y, 0.0F);
            n.setDuration(700L);
            n.setInterpolator(new DecelerateInterpolator());
        }

        if (o == null) {
            o = ObjectAnimator.ofFloat(viewProperty, View.TRANSLATION_Y, wB.a(requireActivity(), (float) viewProperty.getHeight()));
            o.setDuration(300L);
            o.setInterpolator(new DecelerateInterpolator());
        }
    }

    public boolean g() {
        return p;
    }

    private void onRedo() {
        if (!q) {
            HistoryViewBean historyViewBean = cC.c(sc_id).h(projectFileBean.getXmlName());
            if (historyViewBean != null) {
                int actionType = historyViewBean.getActionType();
                if (actionType == HistoryViewBean.ACTION_TYPE_ADD) {
                    for (ViewBean viewBean : historyViewBean.getAddedData()) {
                        jC.a(sc_id).a(projectFileBean.getXmlName(), viewBean);
                    }
                    viewEditor.a(viewEditor.a(historyViewBean.getAddedData(), false), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_UPDATE) {
                    ViewBean prevUpdateData = historyViewBean.getPrevUpdateData();
                    ViewBean currentUpdateData = historyViewBean.getCurrentUpdateData();
                    if (!prevUpdateData.id.equals(currentUpdateData.id)) {
                        currentUpdateData.preId = prevUpdateData.id;
                    }

                    if (currentUpdateData.id.equals("_fab")) {
                        jC.a(sc_id).h(projectFileBean.getXmlName()).copy(currentUpdateData);
                    } else {
                        jC.a(sc_id).c(projectFileBean.getXmlName(), prevUpdateData.id).copy(currentUpdateData);
                    }

                    viewEditor.a(viewEditor.e(currentUpdateData), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_REMOVE) {
                    for (ViewBean viewBean : historyViewBean.getRemovedData()) {
                        jC.a(sc_id).a(projectFileBean, viewBean);
                    }
                    viewEditor.b(historyViewBean.getRemovedData(), false);
                    viewEditor.i();
                } else if (actionType == HistoryViewBean.ACTION_TYPE_MOVE) {
                    ViewBean movedData = historyViewBean.getMovedData();
                    ViewBean viewBean = jC.a(sc_id).c(projectFileBean.getXmlName(), movedData.id);
                    viewBean.copy(movedData);
                    viewEditor.a(viewEditor.b(viewBean, false), false);
                }
            }
            invalidateOptionsMenu();
        }
    }

    public void i() {
        if (projectFileBean != null) {
            b(jC.a(sc_id).d(projectFileBean.getXmlName()));
            a(jC.a(sc_id).h(projectFileBean.getXmlName()));
        }
    }

    public void j() {
        viewEditor.setFavoriteData(Rp.h().f());
    }

    private void invalidateOptionsMenu() {
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    public void l() {
        viewEditor.j();
    }

    private void onUndo() {
        if (!q) {
            HistoryViewBean historyViewBean = cC.c(sc_id).i(projectFileBean.getXmlName());
            if (historyViewBean != null) {
                int actionType = historyViewBean.getActionType();
                if (actionType == HistoryViewBean.ACTION_TYPE_ADD) {
                    for (ViewBean view : historyViewBean.getAddedData()) {
                        jC.a(sc_id).a(projectFileBean, view);
                    }
                    viewEditor.b(historyViewBean.getAddedData(), false);
                    viewEditor.i();
                } else if (actionType == HistoryViewBean.ACTION_TYPE_UPDATE) {
                    ViewBean prevUpdateData = historyViewBean.getPrevUpdateData();
                    ViewBean currentUpdateData = historyViewBean.getCurrentUpdateData();
                    if (!prevUpdateData.id.equals(currentUpdateData.id)) {
                        prevUpdateData.preId = currentUpdateData.id;
                    }
                    if (currentUpdateData.id.equals("_fab")) {
                        jC.a(sc_id).h(projectFileBean.getXmlName()).copy(prevUpdateData);
                    } else {
                        jC.a(sc_id).c(projectFileBean.getXmlName(), currentUpdateData.id).copy(prevUpdateData);
                    }
                    viewEditor.a(viewEditor.e(prevUpdateData), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_REMOVE) {
                    for (ViewBean view : historyViewBean.getRemovedData()) {
                        jC.a(sc_id).a(projectFileBean.getXmlName(), view);
                    }
                    viewEditor.a(viewEditor.a(historyViewBean.getRemovedData(), false), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_MOVE) {
                    ViewBean movedData = historyViewBean.getMovedData();
                    ViewBean viewBean = jC.a(sc_id).c(projectFileBean.getXmlName(), movedData.id);
                    viewBean.preIndex = movedData.index;
                    viewBean.index = movedData.preIndex;
                    viewBean.parent = movedData.preParent;
                    viewBean.preParent = movedData.parent;
                    viewEditor.a(viewEditor.b(viewBean, false), false);
                }
            }
            invalidateOptionsMenu();
        }
    }

    public void n() {
        ArrayList<ViewBean> viewBeanArrayList = eC.a(jC.a(sc_id).d(projectFileBean.getXmlName()));
        ViewBean viewBean;
        if (projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FAB)) {
            viewBean = jC.a(sc_id).h(projectFileBean.getXmlName());
        } else {
            viewBean = null;
        }
        viewProperty.addActivityViews(viewBeanArrayList, viewBean);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        invalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 213) {
            if (resultCode == -1) {
                c(data.getParcelableExtra("bean"));
            }

            if (data != null && data.getBooleanExtra("is_edit_image", false)) {
                for (ViewBean viewBean : jC.a(sc_id).d(projectFileBean.getXmlName())) {
                    c(viewBean);
                }
                if (isFabEnabled) {
                    c(jC.a(sc_id).h(projectFileBean.getXmlName()));
                }
            }
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        viewEditor.setScreenType(newConfiguration.orientation);
        viewEditor.isLayoutChanged = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.design_view_menu, menu);
        menu.findItem(R.id.menu_view_redo).setEnabled(false);
        menu.findItem(R.id.menu_view_undo).setEnabled(false);
        if (projectFileBean != null) {
            if (cC.c(sc_id).f(projectFileBean.getXmlName())) {
                menu.findItem(R.id.menu_view_redo).setEnabled(true);
            } else {
                menu.findItem(R.id.menu_view_redo).setEnabled(false);
            }

            if (cC.c(sc_id).g(projectFileBean.getXmlName())) {
                menu.findItem(R.id.menu_view_undo).setEnabled(true);
            } else {
                menu.findItem(R.id.menu_view_undo).setEnabled(false);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.fr_graphic_editor, parent, false);
        initialize(viewGroup);
        if (bundle != null) {
            sc_id = bundle.getString("sc_id");
        } else {
            sc_id = requireActivity().getIntent().getStringExtra("sc_id");
        }

        return viewGroup;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_view_redo) {
            onRedo();
        } else if (itemId == R.id.menu_view_undo) {
            onUndo();
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle newInstanceState) {
        newInstanceState.putString("sc_id", sc_id);
        super.onSaveInstanceState(newInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (viewProperty != null) {
            viewProperty.d();
        }
    }
}
