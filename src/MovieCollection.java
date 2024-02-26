import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieCollection {
    private List<Movie> movies;
    private Scanner scanner;

    public MovieCollection() {
        movies = new ArrayList<>();
        scanner = new Scanner(System.in);
        startProgram();
    }

    private void startProgram() {
        importDataFromCSV();
        mainMenu();
    }

    private void importDataFromCSV() {
        try {
            Scanner fileScanner = new Scanner(new File("src\\movies_data.csv"));
            while (fileScanner.hasNext()) {
                String[] movieData = fileScanner.nextLine().split(",");
                Movie movie = new Movie(movieData[0], movieData[1].split("\\|"), movieData[2], movieData[3], Integer.parseInt(movieData[4]), Double.parseDouble(movieData[5]));
                movies.add(movie);
            }
            fileScanner.close();
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void mainMenu() {
        String menuOption = "";

        while (!menuOption.equals("q")) {
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (c)ast");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();

            if (menuOption.equals("t")) {
                searchTitles();
            } else if (menuOption.equals("c")) {
                searchCast();
            } else if (menuOption.equals("q")) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
    private void searchTitles() {
        System.out.print("Enter a title search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Movie> matchedMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(searchTerm)) {
                matchedMovies.add(movie);
            }
        }

        if (matchedMovies.isEmpty()) {
            System.out.println("No movie titles match that search term!");
            return;
        }

        for (int i = 0; i < matchedMovies.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < matchedMovies.size(); j++) {
                if (matchedMovies.get(j).getTitle().compareToIgnoreCase(matchedMovies.get(minIndex).getTitle()) < 0) {
                    minIndex = j;
                }
            }
            Movie temp = matchedMovies.get(minIndex);
            matchedMovies.set(minIndex, matchedMovies.get(i));
            matchedMovies.set(i, temp);
        }

        for (int i = 0; i < matchedMovies.size(); i++) {
            System.out.println((i + 1) + ". " + matchedMovies.get(i).getTitle());
        }

        System.out.print("Which movie would you like to learn ore about?\nEnter number: ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > matchedMovies.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Movie selectedMovie = matchedMovies.get(choice - 1);
        System.out.println("Title: " + selectedMovie.getTitle());
        System.out.println("Runtime: " + selectedMovie.getRuntime() + " minutes");
        System.out.println("Director: " + selectedMovie.getDirector());
        System.out.println("Cast: " + selectedMovie.getCast());
        System.out.println("Overview: " + selectedMovie.getOverview());
        System.out.println("User Rating: " + selectedMovie.getUserRating());
    }

    private void searchCast() {
        System.out.print("Enter a person to search for (first or last name): ");
        String searchTerm = scanner.nextLine().toLowerCase();

        String[] matchedCastMembers = new String[movies.size()];
        int matchedCount = 0;

        for (Movie movie : movies) {
            String[] castMembers = movie.getCast();
            for (String castMember : castMembers) {
                if (castMember.toLowerCase().contains(searchTerm)) {
                    boolean alreadyAdded = false;
                    for (int i = 0; i < matchedCount; i++) {
                        if (matchedCastMembers[i].equalsIgnoreCase(castMember)) {
                            alreadyAdded = true;
                            break;
                        }
                    }
                    if (!alreadyAdded) {
                        matchedCastMembers[matchedCount++] = castMember;
                    }
                }
            }
        }

        if (matchedCount == 0) {
            System.out.println("No results match your search.");
            return;
        }

        for (int i = 0; i < matchedCount - 1; i++) {
            for (int j = i + 1; j < matchedCount; j++) {
                if (matchedCastMembers[i].compareToIgnoreCase(matchedCastMembers[j]) > 0) {
                    String temp = matchedCastMembers[i];
                    matchedCastMembers[i] = matchedCastMembers[j];
                    matchedCastMembers[j] = temp;
                }
            }
        }

        for (int i = 0; i < matchedCount; i++) {
            System.out.println((i + 1) + ". " + matchedCastMembers[i]);
        }

        System.out.print("Which would you like to see all movies for?\nEnter number: ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > matchedCount) {
            System.out.println("Invalid choice.");
            return;
        }

        String selectedCastMember = matchedCastMembers[choice - 1];
        String[] moviesWithSelectedCastMember = new String[movies.size()];
        int movieCount = 0;

        for (Movie movie : movies) {
            String[] castMembers = movie.getCast();
            for (String castMember : castMembers) {
                if (castMember.toLowerCase().contains(selectedCastMember.toLowerCase())) {
                    moviesWithSelectedCastMember[movieCount++] = movie.getTitle();
                }
            }
        }

            for (int i = 0; i < movieCount - 1; i++) {
                for (int j = i + 1; j < movieCount; j++) {
                    if (moviesWithSelectedCastMember[i].compareToIgnoreCase(moviesWithSelectedCastMember[j]) > 0) {
                        String temp = moviesWithSelectedCastMember[i];
                        moviesWithSelectedCastMember[i] = moviesWithSelectedCastMember[j];
                        moviesWithSelectedCastMember[j] = temp;
                    }
                }
            }

            for (int i = 0; i < movieCount; i++) {
                System.out.println((i + 1) + ". " + moviesWithSelectedCastMember[i]);
            }

            System.out.print("Which movie would you like to learn more about?\nEnter number: ");
            choice = scanner.nextInt();

            if (choice < 1 || choice > movieCount) {
                System.out.println("Invalid choice.");
                return;
            }

            String selectedMovieTitle = moviesWithSelectedCastMember[choice - 1];
            Movie selectedMovie = null;
            for (Movie movie : movies) {
                if (movie.getTitle().equalsIgnoreCase(selectedMovieTitle)) {
                    selectedMovie = movie;
                    break;
                }
            }

            if (selectedMovie != null) {
                System.out.println("Title: " + selectedMovie.getTitle());
                System.out.println("Runtime: " + selectedMovie.getRuntime() + " minutes");
                System.out.println("Director: " + selectedMovie.getDirector());
                System.out.println("Cast: " + selectedMovie.getCast());
                System.out.println("Overview: " + selectedMovie.getOverview());
                System.out.println("User Rating: " + selectedMovie.getUserRating());
            } else {
                System.out.println("Invalid choice.");
            }
        }
}