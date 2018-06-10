package com.yenimobile.myfavoritetoysudacity.BoardingAppFiles;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yenimobile.myfavoritetoysudacity.R;
import com.yenimobile.myfavoritetoysudacity.databinding.ActivityBoardingpassMainBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BoardingpassMainActivity extends AppCompatActivity {

    ActivityBoardingpassMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_boardingpass_main);

        BoardingPassInfo fakeboardingInfo = FakeDataUtils.generateFakeBoardingPassInfo();

        displayBoardingPassInfo(fakeboardingInfo);
    }


    private void displayBoardingPassInfo(BoardingPassInfo info) {

        mBinding.textViewPassengerName.setText(info.passengerName);
        // COMPLETED (7) Use the flightInfo attribute in mBinding below to get the appropriate text Views
        mBinding.flightInfo.textViewOriginAirport.setText(info.originCode);
        mBinding.flightInfo.textViewFlightCode.setText(info.flightCode);
        mBinding.flightInfo.textViewDestinationAirport.setText(info.destCode);

        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault());
        String boardingTime = formatter.format(info.boardingTime);
        String departureTime = formatter.format(info.departureTime);
        String arrivalTime = formatter.format(info.arrivalTime);

        mBinding.textViewBoardingTime.setText(boardingTime);
        mBinding.textViewDepartureTime.setText(departureTime);
        mBinding.textViewArrivalTime.setText(arrivalTime);

        long totalMinutesUntilBoarding = info.getMinutesUntilBoarding();
        long hoursUntilBoarding = TimeUnit.MINUTES.toHours(totalMinutesUntilBoarding);
        long minutesLessHoursUntilBoarding =
                totalMinutesUntilBoarding - TimeUnit.HOURS.toMinutes(hoursUntilBoarding);

        String hoursAndMinutesUntilBoarding = getString(R.string.countDownFormat,
                hoursUntilBoarding,
                minutesLessHoursUntilBoarding);

        mBinding.textViewBoardingInCountdown.setText(hoursAndMinutesUntilBoarding);
        // COMPLETED (8) Use the boardingInfo attribute in mBinding below to get the appropiate text Views
        mBinding.boardingInfo.textViewTerminal.setText(info.departureTerminal);
        mBinding.boardingInfo.textViewGate.setText(info.departureGate);
        mBinding.boardingInfo.textViewSeat.setText(info.seatNumber);
    }




}
