%{
city_err = calc_error(city_target-rb_net(city_input));
ra_err = calc_error(ripeatlas_target-rb_net(ripeatlas_input));
univ_err = calc_error(univ_target-rb_net(univ_input));

hold all
ra_err(ra_err>20)=[];
cdfplot(ra_err*85);
cdfplot(univ_err*85);
cdfplot(city_err*85);
legend('Ripe Atlas','University','City');
title('');
xlabel('Error distance(km)','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);
%}

load('data_main_sepds.mat');
[city_rb,city_err] = train_rb(city_input,city_target,0.8);
[probe_rb,probe_err] = train_rb(probe_input,probe_target,0.8);
[univ_rb,univ_err] = train_rb(univ_input,univ_target,0.8);
close all;

city_errd = error_dist(city_err);
probe_errd = error_dist(probe_err);
univ_errd = error_dist(univ_err);

probe_errd(probe_errd>5) = [];

hold all
cdfplot(probe_errd*85);
cdfplot(univ_errd*85);
cdfplot(city_errd*85);
legend('Ripe Atlas','University','City');
title('');
xlabel('Error distance(km)','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);