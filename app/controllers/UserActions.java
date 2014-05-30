/*package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.Area;
import models.Department;
import models.KUStatus;
import models.KUnit;
import models.Lever;
import models.Location;
import models.Role;
import models.AppUser;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

@BasicAuth
public class UserActions extends Controller {

	public static Result dashboard() {

		final AppUser appUser = LoginController.getLoggedInUser();


		final List<KUnit> myKuList = KUnit.find.where().eq("author", LoginController.getLoggedInUser()).eq("kuStatus", KUStatus.APPROVED).findList();
		myKuList.addAll(KUnit.find.where().in("collaboratorList.id", LoginController.getLoggedInUser().id).eq("kuStatus", KUStatus.APPROVED).findList());
		final List<KUnit> apprList = new ArrayList<KUnit>();
		final List<KUnit> kuList = new ArrayList<KUnit>();
		List<Location> locList = new ArrayList<Location>();
		List<Department> deptList = new ArrayList<Department>();
		List<Area> areaList = new ArrayList<Area>();
		List<Lever> leverList = new ArrayList<Lever>();

		apprList.addAll(KUnit.find.where().in("approver", LoginController.getLoggedInUser()).findList());

		if(LoginController.getLoggedInUser().role == Role.COORDINATOR){
			final List<AppUser> locationUsers = LoginController.getLoggedInUser().location.userList();
			kuList.addAll(KUnit.find.where().in("author", locationUsers).findList());
		}


		if(LoginController.getLoggedInUser().role == Role.KMPO || LoginController.getLoggedInUser().role == Role.ADMIN){
			locList = Location.find.all();
			deptList = Department.find.all();
			areaList = Area.find.all();
			leverList = Lever.find.all();
		}



		//final List<KUnit> favDeptRecAdded = KUnit.find.where().eq("kuStatus", KUStatus.APPROVED).in("departmentList", appUser.userPreference.departmentList).orderBy("approvedAt desc").findList();
		//final List<KUnit> favDeptTopRated = KUnit.find.where().eq("kuStatus", KUStatus.APPROVED).in("departmentList", appUser.userPreference.departmentList).orderBy("averageRating desc").findList();

		final List<KUnit> favDeptRecAdded = new ArrayList<KUnit>();
		final List<KUnit> favDeptTopRated = new ArrayList<KUnit>();
		for (final Department dept : appUser.userPreference.departmentList) {
			favDeptRecAdded.addAll(dept.recentlyAddedKUnits(5));
			favDeptTopRated.addAll(dept.topRatedKUnits(5));
		}

		final List<KUnit> favAreaRecAdded = new ArrayList<KUnit>();
		final List<KUnit> favAreaTopRated = new ArrayList<KUnit>();
		for (final Area area : appUser.userPreference.areaList) {
			favAreaRecAdded.addAll(area.recentlyAddedKUnits(5));
			favAreaTopRated.addAll(area.topRatedKUnits(5));
		}

		final List<KUnit> favLeverRecAdded = new ArrayList<KUnit>();
		final List<KUnit> favLeverTopRated = new ArrayList<KUnit>();
		for (final Lever lever : appUser.userPreference.leverList) {
			favLeverRecAdded.addAll(lever.recentlyAddedKUnits(5));
			favLeverTopRated.addAll(lever.topRatedKUnits(5));
		}

		final SortKUsByApprovedDate kuSortByDateComparator = new SortKUsByApprovedDate();

		Collections.sort(favDeptRecAdded, kuSortByDateComparator);
		Collections.sort(favAreaRecAdded, kuSortByDateComparator);
		Collections.sort(favLeverRecAdded, kuSortByDateComparator);
		Collections.sort(myKuList, kuSortByDateComparator);

		final SortKUsByRating kuSortByRatingComparator = new SortKUsByRating();
		Collections.sort(favDeptTopRated, kuSortByRatingComparator);
		Collections.sort(favAreaTopRated, kuSortByRatingComparator);
		Collections.sort(favLeverTopRated, kuSortByRatingComparator);

		return ok(views.html.userDashboard.render(
				appUser,
				favDeptRecAdded,
				favAreaRecAdded,
				favLeverRecAdded,
				favDeptTopRated,
				favAreaTopRated,
				favLeverTopRated,
				myKuList,
				apprList,
				kuList,
				locList,
				deptList,
				areaList,
				leverList
				));
	}

}

class SortKUsByApprovedDate implements Comparator<KUnit>{
	@Override
	public int compare(final KUnit ku1, final KUnit ku2) {
		if((ku1.approvedAt == null) || (ku2.approvedAt == null)){
			return -1;
		}
		return ku1.approvedAt.compareTo(ku2.approvedAt);
	}
}

class SortKUsByRating implements Comparator<KUnit>{
	@Override
	public int compare(final KUnit ku1, final KUnit ku2) {
		if((ku1.averageRating == null) || (ku2.averageRating == null)){
			return -1;
		}
		return ku2.averageRating.compareTo(ku1.averageRating);
	}
}
 */