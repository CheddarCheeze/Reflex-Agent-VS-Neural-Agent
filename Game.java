import java.util.ArrayList;
import java.util.Random;

class Game
{

	static double[] evolveWeights()
	{
		// Create a random initial population
		Random r = new Random();
		Matrix population = new Matrix(100, 291);
		for(int i = 0; i < 100; i++)
		{
			double[] chromosome = population.row(i);
			for(int j = 0; j < chromosome.length; j++)
				chromosome[j] = 0.03 * r.nextGaussian();
		}

		// Evolve the population
		// todo: YOUR CODE WILL START HERE.
		//       Please write some code to evolve this population.
		//       (For tournament selection, you will need to call Controller.doBattleNoGui(agent1, agent2).)
                
                Matrix geneResistance = new Matrix(population.rows(), population.cols());
                for(int i = 0; i < geneResistance.rows(); i++)
		{
			double[] genes = geneResistance.row(i);
			for(int j = 0; j < genes.length; j++)
				genes[j] = r.nextInt(5);
		}
                
                // Repeat genetic algorithm
                for(int repeat = 0; repeat < 20; repeat++){
                        
                    // Promote diversity (Mutation)
                    int mutationRate = 61 - (repeat * 3);
                    int chromosomeRate = 81 - (int)(repeat * 4);
                    double mutationDeviation = 0.75;
                    double low = -0.35;
                    double high = 0.35;
                    double value;
                    for(int i = 0; i < population.rows(); i++) {
                        if(r.nextInt(100) < mutationRate) {
                            for(int j = 0; j < population.cols(); j++) {
                                if(r.nextInt(100) < chromosomeRate - geneResistance.row(i)[j]) {
                                    value = r.nextGaussian() * mutationDeviation;
                                    if(value < low) {
                                        population.row(i)[j] += low;
                                    }
                                    else if(high < value) {
                                        population.row(i)[j] += high;
                                    }
                                    else {
                                        population.row(i)[j] += value;
                                    }
                                }
                            }
                        }
                    }

                    // Natural selection
                    int numOfTournaments = 30;
                    int killLoserRate = 85;
                    int robot1;
                    int robot2;
                    int winner = 0;
                    int[] replaceChild = new int[numOfTournaments];
                    int[] winnerChild = new int[numOfTournaments];
                    for(int i = 0; i < numOfTournaments; i++) {
                        robot1 = r.nextInt(population.rows());
                        robot2 = r.nextInt(population.rows());
                        while(robot1 == robot2) {
                            robot2 = r.nextInt(population.rows());
                        }
                        try {
                            winner = Controller.doBattleNoGui(new NeuralAgent(population.row(robot1)), new NeuralAgent(population.row(robot2)));
                            if(r.nextInt(100) < killLoserRate) {
                                // Kill the loser
                                if(winner == 1) {
                                    replaceChild[i] = robot2;
                                    winnerChild[i] = robot1;
                                    for(int k = 0; k < geneResistance.cols(); k++) {
                                        geneResistance.row(robot1)[k] = 100;
                                        geneResistance.row(robot2)[k] = r.nextInt(5);
                                    }
                                }
                                else {
                                    replaceChild[i] = robot1;
                                    winnerChild[i] = robot2;
                                    for(int k = 0; k < geneResistance.cols(); k++) {
                                        geneResistance.row(robot2)[k] = 100;
                                        geneResistance.row(robot1)[k] = r.nextInt(5);
                                    }
                                }
                            }
                            else {
                                // Kill the winner
                                if(winner == 1) {
                                    replaceChild[i] = robot1;
                                    winnerChild[i] = robot2;
                                }
                                else {
                                    replaceChild[i] = robot2;
                                    winnerChild[i] = robot1;
                                }
                            }
                        }
                        catch(Exception e) {
                            System.out.println("Could not do battle.");
                        }
                    }

                    // Replenish population
                    for(int i = 0; i < replaceChild.length; i++) {
                        population.copyBlock(replaceChild[i], 0, population, winnerChild[i], 0, 1, (population.cols()/2));
                        population.copyBlock(replaceChild[i], 50, population, winnerChild[(i+1)%replaceChild.length], 0, 1, (population.cols()/2));
//                        population.copyBlock(replaceChild[i], 20, population, winnerChild[(i+2)%replaceChild.length], 0, 1, (population.cols()/10));
//                        population.copyBlock(replaceChild[i], 30, population, winnerChild[(i+3)%replaceChild.length], 0, 1, (population.cols()/10));
//                        population.copyBlock(replaceChild[i], 40, population, winnerChild[(i+4)%replaceChild.length], 0, 1, (population.cols()/10));
//                        population.copyBlock(replaceChild[i], 50, population, winnerChild[(i+5)%replaceChild.length], 0, 1, (population.cols()/10));
//                        population.copyBlock(replaceChild[i], 60, population, winnerChild[(i+6)%replaceChild.length], 0, 1, (population.cols()/10));
//                        population.copyBlock(replaceChild[i], 70, population, winnerChild[(i+7)%replaceChild.length], 0, 1, (population.cols()/10));
//                        population.copyBlock(replaceChild[i], 80, population, winnerChild[(i+8)%replaceChild.length], 0, 1, (population.cols()/10));
//                        population.copyBlock(replaceChild[i], 90, population, winnerChild[(i+9)%replaceChild.length], 0, 1, (population.cols()/10));
                    }

                }
                
                int chosenWarrior = 0;
                int numWinners = 0;
                int numLosers = 0;
                int numDraw = 0;
                for(int i = 0; i < population.rows(); i++) {
                    try{
                        int winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(population.row(i)));
                        switch (winner) {
                        case -1:
                            //System.out.println("Red Wins!");
                            numWinners++;
                            chosenWarrior = i;
                            break;
                        case 1:
                            //System.out.println("Blue Wins!");
                            numLosers++;
                            break;
                        default:
                            //System.out.println("Draw!");
                            numDraw++;
                            break;
                        }
                    }
                    catch(Exception e) {

                    }
                }
                System.out.println("Number of winners: " + numWinners);
                System.out.println("Number of losers: " + numLosers);
                System.out.println("Number of draws: " + numDraw);
                
                Vec.copy(population.row(0), population.row(chosenWarrior));
                
		// Return an arbitrary member from the population
		return population.row(0);
	}


	public static void main(String[] args) throws Exception
	{
		double[] w = evolveWeights();
//                double[] w = {-0.10962615414408776,
//0.17587758304184842,
//0.3106085135038879,
//0.040833927817017085,
//-0.18179573219796388,
//-0.38333596080450116,
//0.1862787792406545,
//0.21721585719832337,
//0.46729763813083647,
//0.025308709432783294,
//-0.05636141402789397,
//-0.471817401903415,
//0.16407274104860167,
//0.023196285346174572,
//0.5747084125438265,
//-0.38373818099557633,
//0.15527798092593076,
//0.37022498914076046,
//0.5858508291501975,
//0.017367615775549638,
//0.05347978612113641,
//-0.012622719344478561,
//-0.01073300590157678,
//-0.08043115710191531,
//-0.461141492121142,
//-0.010160373721896886,
//0.24446321727840298,
//0.2880861318361667,
//-0.2194955437376236,
//0.5168399217971608,
//-0.0020545435520312526,
//-0.21129672240670344,
//-0.0018595520175670247,
//0.08541764612254951,
//-0.28725437352655697,
//-0.25598422171272545,
//0.321335307616979,
//-0.24261202742882051,
//-0.2743034382654724,
//0.337418758781496,
//-0.08074409961593325,
//-0.47337878744733464,
//-0.04536927859593621,
//-0.05285245164096743,
//0.3796310194732708,
//0.2993113505514063,
//-0.04498199632727519,
//0.06566199181530749,
//-0.25437111081493524,
//-0.32905764389076864,
//0.2450542214789649,
//0.001503650445629632,
//0.16887749754199136,
//0.10905787716861173,
//0.10073132453672093,
//-0.01947002166142639,
//0.05578145121783158,
//-0.26990155510893443,
//-0.926638119283652,
//-0.1527720209528887,
//-0.1775475185251419,
//0.005925622134176854,
//2.3067352310821368E-4,
//0.14928294598088457,
//-0.06666575834220548,
//0.15361509462678266,
//-0.27691579372384656,
//-0.6618147178581658,
//-0.123286256830254,
//-0.03332866462701467,
//-0.21016541109625178,
//-0.2002117832272403,
//-0.08059208526425406,
//0.35558226473655297,
//-0.1774865611344476,
//0.10974722881778173,
//-0.34286339359531753,
//-0.04691125319198037,
//0.04914710202966891,
//0.28036143293638716,
//-0.0032757649712261278,
//0.6239764415006688,
//-0.0967306068985225,
//0.36506992528730464,
//-0.4977180188811676,
//-0.0026835647785234964,
//-0.12447930818772648,
//-0.022860110680805235,
//0.1975928514345507,
//-9.840400655011822E-4,
//0.26820285637146424,
//0.28798168791463336,
//0.3280337215761146,
//0.174105546507268,
//-0.41939543170699856,
//0.024076820827370754,
//-0.019322995680915653,
//0.3929851029111435,
//0.33916094927898105,
//0.056632769768138386,
//-0.5196476160997933,
//0.051196592498275156,
//-0.3475283838714053,
//-0.2603069015022272,
//-0.35046823762929885,
//-0.7074719210377544,
//0.2759311101059857,
//-0.5488225293491852,
//-0.24899812828182522,
//0.1399228357681854,
//0.007687125883413472,
//-0.24093503990778956,
//0.01136699489999016,
//-0.14748574997476505,
//-0.3440226202198305,
//-0.34038077118982635,
//0.05062323959547362,
//-0.6152128633330083,
//0.5016886333136825,
//-0.011330177872663527,
//-0.492451886624597,
//0.001512389065262798,
//-0.22667887225324052,
//-0.10046849130684185,
//0.08666092760859112,
//0.6532793463979115,
//-0.17461796067275598,
//0.37942180559683386,
//-0.06593682375737789,
//-0.05386387567700581,
//0.25720269139315105,
//-0.5876917812093733,
//-0.02929624885268388,
//-0.2575087416609908,
//-0.2430866227669128,
//0.07588628066512008,
//0.20092608891480845,
//0.1674493863646853,
//0.1561276220546757,
//0.05510412250679284,
//0.18135628311313473,
//0.3163272529298306,
//0.22888532987212767,
//-0.3413379767092397,
//0.007974305535738814,
//-0.21560936804718428,
//-0.02781785255923298,
//-0.0013558846113705947,
//0.1806960568016553,
//-0.549723354017795,
//-0.056020743697573164,
//0.19537146487047083,
//0.016816456231125005,
//-0.3141591029177321,
//-0.20967067649444388,
//-0.04259599448487994,
//0.2361616583219428,
//-0.4794199704156332,
//-0.09898538713205958,
//-0.020523159812845686,
//0.045791560427240224,
//-0.38199339124501874,
//-0.0236410547788067,
//0.007079522758664761,
//-0.3027018279935365,
//0.04771348606468511,
//0.24262171317537104,
//-0.3163613784692732,
//-0.1963542607210546,
//-0.18510179765375695,
//0.02030876369026933,
//0.2166538186662749,
//0.3381782700778155,
//0.6412376054194051,
//0.09661445240132933,
//0.3823606343890166,
//-0.0786933793031217,
//-0.30869891915152975,
//-0.007895631108498367,
//0.008192774529693992,
//0.3820399217672943,
//-0.020109229638477347,
//-0.023638211516766115,
//-0.13413891223172544,
//-0.025703779293184792,
//-0.3559783578991072,
//-0.00209071459966511,
//-0.26806339159889025,
//-0.046495441986761074,
//-0.022068411614327578,
//0.06474676407938287,
//0.06428120991787653,
//-0.06257543755747533,
//-0.01706581523752349,
//-0.30981245427344756,
//-0.2299605371057892,
//0.01973925033272935,
//0.3318333638981209,
//-0.15211653627371757,
//0.3228453298420004,
//0.6283624161003617,
//-0.20507196655979934,
//0.4928559603100681,
//-0.29698589564091415,
//0.3692635449030912,
//0.38023176755495236,
//-0.006159752630592493,
//0.017350470544222757,
//-0.07364072639097549,
//-0.12505427353015713,
//-0.09909893737246522,
//-0.08061629924144711,
//-0.5355311328455994,
//0.024475970452869925,
//-0.026749229176115188,
//-0.11838945421574121,
//0.3775105220471072,
//-0.1816678691893102,
//0.027475897765678586,
//-0.21342210685116775,
//0.22969451223014173,
//0.37365247017469594,
//-0.3359766102975119,
//0.30614624381140926,
//-0.21863578492360008,
//-0.022019012051703205,
//-6.183630935128202E-4,
//0.018418350143907247,
//-0.30158987812240645,
//-0.18453392446470593,
//0.15874020307931702,
//-0.011376458488823047,
//-0.01288354126563713,
//-0.4322050268097541,
//-0.23823391953512513,
//0.22050397798892307,
//0.2885835205236585,
//-0.3415962796057376,
//0.18628352176385274,
//0.41523707198390825,
//0.17392265282417024,
//-0.2336231391521378,
//0.008974058491601138,
//-0.36244860183850197,
//-0.002870466147393517,
//0.3348098221411096,
//-0.10951558142189183,
//0.30348955492343666,
//-0.6666044225835697,
//-0.011833290171560096,
//0.01377437442632605,
//0.0070943569313592705,
//0.36211012851675844,
//0.03941811894556621,
//0.10210913440910488,
//0.09217285175909695,
//0.915335849245972,
//-0.3435466980028288,
//-0.2104418418587849,
//0.7130387936460801,
//0.245340783883516,
//0.08823623521172268,
//-0.2707726885577465,
//-0.08414469483108467,
//1.8286929324204767E-4,
//-0.05225885815780381,
//0.3951671961294222,
//-0.4325233213209362,
//0.2550497538330322,
//-0.054810888160780495,
//0.0018130641634754112,
//0.19517610743459163,
//-0.11350320590670687,
//-0.15369949408182335,
//-0.16688075196753033,
//0.3377778754360746,
//0.2800176426142817,
//0.1768642542658797,
//-0.3357771157104449,
//0.016179529549831345,
//-0.11802319071737319,
//0.23491185002802248,
//0.061747452772910896,
//-0.31149908422175576,
//-0.28050123527030874,
//0.18847181571578975,
//0.049276522190810836,
//0.020201767005538895,
//0.25623998695744044,
//0.1966382464947148,
//0.033131183521727586};
                
		Controller.doBattle(new ReflexAgent(), new NeuralAgent(w));
		int winner = Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(w));
                switch (winner) {
                    case -1:
                        System.out.println("Red Wins!");
                        
//                for(int i = 0; i < w.length; i++) {
//                    System.out.println(w[i] + ",");
//                }
//                System.out.println();
                        break;
                    case 1:
                        System.out.println("Blue Wins!");
                        break;
                    default:
                        System.out.println("Draw!");
                        break;
            }
	}

}
