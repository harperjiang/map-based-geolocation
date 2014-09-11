% The relationship between error and landmark densitiy
% First I split t

err020 = calc_error(lden_0_20_target-rb_net(lden_0_20_input));
err2150 = calc_error(lden_21_50_target-rb_net(lden_21_50_input));
err51100 = calc_error(lden_51_100_target-rb_net(lden_51_100_input));
err101inf = calc_error(lden_101_inf_target-rb_net(lden_101_inf_input));

hold all
err020(err020>20)=[];
cdfplot(err020*85);
cdfplot(err2150*85);
cdfplot(err51100*85);
cdfplot(err101inf*85);
legend('> 100','51-100','21-50','0-20');
title('');
xlabel('Error distance(km)','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);