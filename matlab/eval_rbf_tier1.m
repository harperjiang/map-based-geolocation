
load('data_main.mat');
load('data_network.mat');
%{
err = error_dist(target - rb_net(input));
cdfplot(err*85);
%}

%{
city_err = error_dist(city_target-rb_net(city_input));
probe_err = error_dist(probe_target-rb_net(probe_input));
univ_err = error_dist(univ_target-rb_net(univ_input));

hold all
probe_err(probe_err>20)=[];
cdfplot(probe_err*85);
cdfplot(univ_err*85);
cdfplot(city_err*85);
legend('Ripe Atlas','University','City');
title('');
xlabel('Error distance(km)','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);
%}
%{
[rb,err] = train_rb(input,target,0.65);
close all;
errd = error_dist(err);
cdfplot(errd*85);
%}

[city_rb,city_err] = train_rb(city_input,city_target,1.3);
[probe_rb,probe_err] = train_rb(probe_input,probe_target,1.3);
[univ_rb,univ_err] = train_rb(univ_input,univ_target,1.3);
close all;

city_errd = error_dist(city_err);
probe_errd = error_dist(probe_err);
univ_errd = error_dist(univ_err);

probe_errd(probe_errd>10) = [];
univ_errd(univ_errd>10) = [];
hold all
cdfplot(probe_errd*85);
cdfplot(univ_errd*85);
cdfplot(city_errd*85);
legend('Ripe Atlas','University','City');
title('');
xlabel('Error distance(km)','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);
