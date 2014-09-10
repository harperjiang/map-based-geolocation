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