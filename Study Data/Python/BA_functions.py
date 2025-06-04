#####################################################
# Functions for BA
#####################################################

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import matplotlib.ticker as ticker


"""
Functions to rename Columns
""" 
# rename columns for initial_answers.csv
def rename_columns_initialAnswers(initial_answers_df):
    rename_dict = {
        'age': 'Age',
        'androidID': 'Android_ID',
        'androidVersion': 'Android_Version',
        'language': "Language", 
        'manufacturer': 'Manufacturer',
        'pid': 'Prolific_ID',
        'sex': 'Gender',
        'timestamp': 'Timestamp'
    }
    return initial_answers_df.rename(columns=rename_dict).drop(columns=[col for col in rename_dict if rename_dict[col] is None])

# rename columns for intervention_answers.csv
def rename_columns_interventionAnswers(intervention_answers_df):
    rename_dict = {
        'ack': 'Ack',
        'androidID': 'Android_ID',
        'appName': 'App_Name',
        'atHome': 'At_Home',
        'currAct':'Current_Activity',
        'delayTimeInSeconds': 'Latency',
        'goal_alignment': 'Goal_Alignment',
        'interventionType': 'Intervention_Type',
        'kss': 'Sleepiness',
        'pid': 'Prolific_ID',
        'rshci1': 'Rshci 1',
        'rshci2': 'Rshci 2',
        'rshci3': 'Rshci 3',
        'rshci4': 'Rshci 4',
        'rshci5': 'Rshci 5',
        'sam': 'Valence',
        'satisfaction': 'Satisfaction',
        'sense_of_agency': 'Agency',
        'sideActivity': 'Multitasking',
        'situation': 'Social_Situation',
        'stress': 'Stress',
        'timestamp': 'Timestamp',
        'useful_in_situation': 'Usefulness'
    }
    return intervention_answers_df.rename(columns=rename_dict).drop(columns=[col for col in rename_dict if rename_dict[col] is None])

# rename columns for delay_time.csv
def rename_columns_delay_time(delay_time_df):
    rename_dict = {
        'Android ID': 'Android_ID',
        'appName': 'App_Name',
        'delayTime': 'Latency',
        'delayTimeFormatted': "delayTimeFormatted",  # Remove column
        'pID': 'Prolific_ID',
        'timestamp': 'Timestamp'
    }
    return delay_time_df.rename(columns=rename_dict).drop(columns=[col for col in rename_dict if rename_dict[col] is None])

# rename columns for final.csv
def rename_columns_final(finalQ_df):
    rename_dict = {
        'pid_val': 'Prolific_ID',
        'last_checkout_format' : 'timestamp'
    }
    return finalQ_df.rename(columns=rename_dict).drop(columns=[col for col in rename_dict if rename_dict[col] is None])

# rename columns for results-survey.csv
def rename_columns_surveyresults(surveyresults_df):
    rename_dict = {
        'pid': 'Prolific_ID',
    }
    return surveyresults_df.rename(columns=rename_dict).drop(columns=[col for col in rename_dict if rename_dict[col] is None])



"""
Help Functions
"""
# finds a specific Prolific_ID 
def find_prolific_id_in_dataset(Prolific_ID, df):
    """
    Searches for a specific Prolific ID within a single dataframe and prints whether it is found.
    
    Parameters:
    - Prolific_ID (str): The Prolific ID to search for.
    - df (pd.DataFrame): DataFrame to search within.
    
    Returns:
    - None: Outputs the result of the search to the console.
    """
    # Check for the presence of the Prolific ID in the DataFrame
    in_df = Prolific_ID in df['Prolific_ID'].values
    
    # Print results
    print(f"Prolific ID '{Prolific_ID}' in DataFrame: {'Found' if in_df else 'Not Found'}")


#
# remove invalid data
#
# removes false attention checks from intervention df
def remove_false_attention_checks(intervention_df):
    """
    Removes all entries from a DataFrame where the 'Ack' column is False.
    
    Parameters:
    - intervention_df (DataFrame): The DataFrame to process.
    
    Returns:
    - DataFrame: A DataFrame with entries removed where 'Ack' is False.
    """
    # Record the initial length of the DataFrame
    initial_length = len(intervention_df)
    
    # Remove entries where 'Ack' is False
    cleaned_df = intervention_df[intervention_df['Ack'] == True]
    
    # Record the final length and calculate the number of deleted entries
    final_length = len(cleaned_df)
    deleted_entries = initial_length - final_length
    
    print(f"\nDeleted {deleted_entries} entries where the Questionnaire Attention Check was False.")
    
    return cleaned_df

# remove false attention checks from survey df an print pids
def remove_false_attention_checksSurvey(survey_df):
    # Define valid responses for attention checks
    valid_responses = ['5 (very much)', '4']

    # Record the initial length of the DataFrame
    initial_length = len(survey_df)
    
    # Filter DataFrame for valid attention checks
    valid_df = survey_df[survey_df['SelfControl[SCATC]'].isin(valid_responses)]
    
    # Record the final length and calculate the number of deleted entries
    final_length = len(valid_df)
    deleted_entries = initial_length - final_length
    
    # Print detailed info about entries that failed the check
    if deleted_entries > 0:
        failed_entries = survey_df[~survey_df['SelfControl[SCATC]'].isin(valid_responses)]
        print(f"Deleted {deleted_entries} entries where the LimeSurvey Attention Check was not passed.")
        print("Details of failed entries:")
        print(failed_entries[['Prolific_ID', 'SelfControl[SCATC]']])
    
    # Return the valid DataFrame
    return valid_df

# removes entries to specific Prolific_IDs (e.g. if there is missing data)
def remove_entries_and_save(df, prolific_ids, filepath):
    """
    Removes all entries with specified Prolific_IDs from the DataFrame and saves the updated DataFrame to a CSV file.

    Parameters:
    - df (DataFrame): The DataFrame to update.
    - prolific_ids (list of str): A list of Prolific_IDs for which entries should be deleted.
    - filepath (str): The file path where the updated DataFrame should be saved.

    Returns:
    - None
    """
    initial_length = len(df)
    df = df[~df['Prolific_ID'].isin(prolific_ids)]
    final_length = len(df)
    deleted_count = initial_length - final_length

    print(f"Deleted {deleted_count} entries for Prolific_IDs: {', '.join(prolific_ids)}. (Missing LimeSurvey Data)")

    # Save the updated DataFrame to CSV
    df.to_csv(filepath, index=False)
    print(f"Updated DataFrame saved to {filepath}")



# cleares App_Name columns where the entry is "InfiniteScape"
def clear_app_name_for_infinite_scape(interventions_df):
    # find entrys with 'InfiniteScape' in 'App_Name' and clean
    mask = interventions_df['App_Name'] == 'InfiniteScape'
    count_infinitescape = mask.sum()
    interventions_df.loc[mask, 'App_Name'] = ""  # Bereinige 'App_Name', wo es 'InfiniteScape' ist
    print(f"\nApp_Name cleared where it was 'InfiniteScape': {count_infinitescape}")
    
    return interventions_df


#
# add missing data
#
# add missing demographic information
def update_demographic_info_and_save(df, filepath):
    """
    Updates demographic information for specific Prolific IDs and saves the DataFrame to a CSV file.

    Parameters:
    - df (DataFrame): The DataFrame to update.
    - filepath (str): The file path where the DataFrame should be saved.

    Returns:
    - None
    """
    # Dictionary with Prolific_ID as keys and tuples of (Age, Gender) as values
    updates = {
        '5d5407dc922257000153657f': (39, 'Female'),
        '62fc25ef4ce0d3d169b7a120': (33, 'Female')
    }

    # Apply updates
    for pid, (age, gender) in updates.items():
        df.loc[df['Prolific_ID'] == pid, 'Age'] = age
        df.loc[df['Prolific_ID'] == pid, 'Gender'] = gender

    # Save the updated DataFrame to CSV, overwriting the existing file
    df.to_csv(filepath, index=False)
    print(f"Updated DataFrame saved to {filepath}")

# add missing LimeSurvey columns 
def concatenate_dataframes(df1, df2):
    """
    Concatenates two DataFrame objects vertically, assuming they have the same column structure.

    Parameters:
    - df1 (DataFrame): The first DataFrame.
    - df2 (DataFrame): The second DataFrame.

    Returns:
    - DataFrame: The concatenated DataFrame.
    """
    # Concatenate the DataFrames
    concatenated_df = pd.concat([df1, df2], ignore_index=True)
    concatenated_df.to_csv("results-survey2.csv", index=False)

    return concatenated_df


"""
Payement Oveview
"""
### prints a payment overview (from first timestamp in initial_answers to last timestamp in delay_time)
# if there is no initial_nswers Data, calculate from first timestamp in delay_time to last timestamp in delay_time
def paymentOverview(initial_df, delay_df, intervention_df_checked, final_df):
    print("\nPayment Overview:\n")

    # check, if 'Prolific ID' is in all DataFrames
    for df, name in zip([initial_df, delay_df, intervention_df_checked, final_df], ['initial', 'delayTime', 'intervention', 'final']):
        if 'Prolific_ID' not in df.columns:
            print(f"The {name} DataFrame does not contain a 'Prolific ID' column.")
            return

    # Convert 'Timestamp' to datetime in both DataFrames
    current_year = pd.to_datetime('today').year
    initial_df['Timestamp'] = pd.to_datetime(initial_df['Timestamp'].apply(lambda x: f"{current_year} {x}"), format='%Y %b %d %H:%M:%S', errors='coerce')
    delay_df['Timestamp'] = pd.to_datetime(delay_df['Timestamp'].apply(lambda x: f"{current_year} {x}"), format='%Y %b %d %H:%M:%S')

    # Get the earliest and latest Timestamps
    initial_timestamps = initial_df.groupby('Prolific_ID')['Timestamp'].min()
    earliest_delay = delay_df.groupby('Prolific_ID')['Timestamp'].min()
    latest_delay = delay_df.groupby('Prolific_ID')['Timestamp'].max()

    # Calculate the days since initial timestamp to current date
    days_since_initial = (pd.to_datetime('today') - initial_timestamps).dt.days

    # Calculate the participation days by comparing the first and last timestamps
    participation_days = {}
    start_dates = {}  # Speichert das Startdatum jedes Teilnehmers
    for pid in latest_delay.index:
        initial_time = initial_timestamps.get(pid, pd.NaT)
        start_time = initial_time if not pd.isna(initial_time) else earliest_delay[pid]
        participation_days[pid] = (latest_delay[pid] - start_time).total_seconds() / (3600 * 24)
        participation_days[pid] = f"{participation_days[pid]:.2f}"
        start_dates[pid] = start_time 

    participation_days = pd.Series(participation_days)


    # add participants from initial_df if not in in delay_df
    for pid in initial_df['Prolific_ID'].unique():
        if pid not in participation_days:
            participation_days[pid] = "0.00*"


    # Final IDs from final_df
    final_completed_ids = set(final_df['Prolific_ID'].unique())

    # Identifying participants who have completed 7 or more days or are in final_df
    completed_ids = set(participation_days[participation_days.str.replace("*", "", regex=False).astype(float) >= 7].index)
    completed_ids.update(final_completed_ids)

    # Counting valid Attention Checks
    valid_att_checks = intervention_df_checked[intervention_df_checked['Ack'] == True]['Prolific_ID'].value_counts()

    # Count entries
    delay_counts = delay_df['Prolific_ID'].value_counts()
    intervention_counts = intervention_df_checked['Prolific_ID'].value_counts()

    # Sorting participant IDs by participation days in descending order
    sorted_participants = participation_days.sort_values(ascending=False).index.tolist()


    # Initialize cost calculation
    total_costs = 0

    # Table headers
    print("Participants Who Completed the Study (sorted by days participated):")
    print("="*120)
    print(f"{'Prolific ID': <25} | {'Total Triggers': <15} | {'Total Questionnaires': <20} | {'%': <6} | {'Total Reward (£)': <15} | {'Days Participated': <17} | {'Days Since Start': <17}")

    # Participants who completed or are marked in final_df
    for uid in sorted_participants:
        if uid in completed_ids:
            final_marker = "#" if uid in final_completed_ids else ""
            total_intervention_for_id = intervention_counts.get(uid, 0)
            total_delay_for_id = delay_counts.get(uid, 0)
            valid_checks_for_id = valid_att_checks.get(uid, 0)
            days_participated = participation_days[uid]
            days_since_start = days_since_initial.get(uid, "N/A")
            percentage = (total_intervention_for_id / total_delay_for_id) * 100 if total_delay_for_id else 0
            total_reward = valid_checks_for_id * 0.5
            total_costs += total_reward
            print(f"{uid + final_marker: <25} | {total_delay_for_id: <15} | {valid_checks_for_id: <20} | {percentage:.2f}%  | £{total_reward: <15.2f} | {days_participated} | {days_since_start}")

    print(f"\nTotal Costs for Completed Participants: £{total_costs:.2f}")
    print(f"Total Participants Who Completed: {len(completed_ids)}")

    # Participants who did not complete and are not in final_df
    print("\nParticipants Who Did Not Complete the Study:")
    print("="*120)
    for uid in sorted_participants:
        if uid not in completed_ids:
            total_intervention_for_id = intervention_counts.get(uid, 0)
            total_delay_for_id = delay_counts.get(uid, 0)
            valid_checks_for_id = valid_att_checks.get(uid, 0)
            days_participated = participation_days[uid]
            days_since_start = days_since_initial.get(uid, "N/A")
            percentage = (total_intervention_for_id / total_delay_for_id) * 100 if total_delay_for_id else 0
            total_reward = valid_checks_for_id * 0.5
            print(f"{uid: <25} | {total_delay_for_id: <15} | {valid_checks_for_id: <20} | {percentage:.2f}%  | £{total_reward: <15.2f} | {days_participated} | {days_since_start}")

    print(f"\nTotal Costs for Participants Who Did Not Complete: £{total_costs:.2f}")
    print(f"Total Participants Who Did Not Complete: {len(sorted_participants) - len(completed_ids)}")

    # Dictionary mit Start- und Enddatum (Startdatum + 7 Tage)
    #completed_ids = {pid for pid in completed_ids if pid in start_dates}
    completed_data = {
        pid: (start_dates[pid], start_dates[pid] + pd.Timedelta(days=7)) for pid in completed_ids if pid in start_dates
    }

    return completed_data  # Rückgabe von {Prolific_ID: (Startdatum, Enddatum)}


"""
Registration Overview
"""
# compare prolific ids to track who is registered 
# (LimeSurvey download: Fragencode, nur komplette Antwortsätze)
def trackRegistration(initial_df, survey_df):
    print("\n\nRegistration Overview:\n")
    
    # Sort the survey dataframe by 'id'
    survey_df = survey_df.sort_values(by='id')

    # 'pid' is the column in the survey data
    # and 'Prolific_ID' is the column in the initial answers data
    survey_ids = survey_df['Prolific_ID'].dropna().unique()
    initial_ids = initial_df['Prolific_ID'].dropna().unique()

    # Find the intersection and differences
    matching_ids = set(survey_ids).intersection(initial_ids)
    non_matching_initial_ids = set(initial_ids) - set(survey_ids)
    non_matching_survey_ids = set(survey_ids) - set(initial_ids)

    # Filter for survey results where 'lastpage' is 3
    completed_page_three = survey_df[survey_df['lastpage'] == 3]
    completed_page_three_ids = set(completed_page_three['Prolific_ID'].dropna().unique())

    # Find non-matching IDs that also completed page 3
    non_matching_and_completed = non_matching_survey_ids.intersection(completed_page_three_ids)

    # Print the results
    print(f"\nParticipants who have downloaded the app and finished LimeSurvey: {len(matching_ids)}")
    matching_sorted = survey_df[survey_df['Prolific_ID'].isin(matching_ids)]
    for pid in matching_sorted['Prolific_ID']:
        print(pid)
    
    print(f"\nParticipants who have downloaded the app but didn't finish LimeSurvey: {len(non_matching_initial_ids)}")
    #print(f"(PilotStudy: 12)")
    for pid in non_matching_initial_ids:
        print(pid)

    print(f"\nAll Participants who have not downloaded the app: {len(non_matching_survey_ids)}")
    non_matching_survey_sorted = survey_df[survey_df['Prolific_ID'].isin(non_matching_survey_ids)]
    for pid in non_matching_survey_sorted['Prolific_ID']:
        print(pid)
    
    #print(f"\nParticipants who have not downloaded the app and finished LimeSurvey: {len(non_matching_and_completed)}")
    #non_matching_and_completed_sorted = survey_df[survey_df['Prolific_ID'].isin(non_matching_and_completed)]
    #for pid in non_matching_and_completed_sorted['Prolific_ID']:
    #    print(pid)
    
    #return matching_ids, non_matching_survey_ids, non_matching_initial_ids, non_matching_and_completed

# report reasons why not installed
def report_reasons(survey_df, ids):
    """
    Prints reasons for 'whyNotInstalled' or 'whyNotIntereseted' for given IDs.

    Parameters:
    - survey_df (DataFrame): DataFrame containing survey data.
    - ids (list): List of IDs to look up in the DataFrame.

    Returns:
    - None: Prints the reasons directly.
    """

    print("\nReason:\n")

    # Filter the DataFrame for given IDs
    filtered_df = survey_df[survey_df['Prolific_ID'].isin(ids)]

    # Loop through each ID and print the relevant information
    for pid in ids:
        # Get the row corresponding to the current ID
        row = filtered_df[filtered_df['Prolific_ID'] == pid]

        if not row.empty:
            why_not_installed = row['whyNotInstalled'].iat[0] if 'whyNotInstalled' in row.columns and not row['whyNotInstalled'].isna().iat[0] else ""
            why_not_interested = row['whyNotIntereseted'].iat[0] if 'whyNotIntereseted' in row.columns and not row['whyNotIntereseted'].isna().iat[0] else ""
            
            print(f"ID {pid}:")
            print(f"  whyNotInstalled: {why_not_installed}")
            print(f"  whyNotIntereseted: {why_not_interested}")
        else:
            print(f"ID {pid}: No data found.")


"""
Merge, clean and export valid Data
"""
# clean Survey Columns and merge with demographic and additional survey data
def preprocess_and_export_validData(intervention_df, initial_df, survey_df):
    """
    Processes survey data by cleaning specified columns and merging with demographic and
    additional survey data, then exporting to a CSV file.

    Parameters:
    - intervention_df (DataFrame): Contains the intervention data.
    - initial_df (DataFrame): Contains demographic data such as 'Gender' and 'Age'.
    - survey_df (DataFrame): Contains survey responses that may need cleaning.

    Returns:
    - None: Saves the cleaned and merged data to 'validData.csv'.
    """

    print("\nProcessing Data...")

    # Define columns to clean in the survey data
    columns_to_clean = [
        'Impulsivity[Imp1]', 'Impulsivity[Imp2]', 'Impulsivity[Imp3]', 'Impulsivity[Imp4]', 'Impulsivity[Imp5]',
        'Impulsivity[Imp6]', 'Impulsivity[Imp7]', 'Impulsivity[Imp8]', 'Impulsivity[Imp9]', 'Impulsivity[Imp10]',
        'Impulsivity[Imp11]', 'Impulsivity[Imp12]', 'Impulsivity[Imp13]', 'Impulsivity[Imp14]', 'Impulsivity[Imp15]',
        'SelfControl[SC1]', 'SelfControl[SC2]', 'SelfControl[SC3]', 'SelfControl[SC4]', 'SelfControl[SC5]',
        'SelfControl[SC6]', 'SelfControl[SC7]', 'SelfControl[SC8]', 'SelfControl[SC9]',
        'SelfControl[SC10]', 'SelfControl[SC11]', 'SelfControl[SC12]', 'SelfControl[SC13]',
        'SelfControl[SCATC]',
        'FOMO[Fomo1]', 'FOMO[Fomo2]', 'FOMO[Fomo3]', 'FOMO[Fomo4]', 'FOMO[Fomo5]',
        'Anxiety[Anx1]', 'Anxiety[Anx2]', 'Anxiety[Anx3]', 'Anxiety[Anx4]', 'Anxiety[Anx5]'
    ]

    # Clean survey response columns
    for col in columns_to_clean:
        if col in survey_df.columns:
            survey_df[col] = survey_df[col].astype(str).str.extract('(\d+)').astype(float)
        else:
            print(f"Warning: {col} does not exist in the survey DataFrame.")

    # Merge with initial data to include 'Gender' and 'Age'
    valid_data = intervention_df.merge(initial_df[['Prolific_ID', 'Gender', 'Age']], on='Prolific_ID', how='left')

    # Exclude unnecessary columns from the survey data
    exclude_columns = ["id", "lastpage", "startlanguage", "seed", "startdate", "submitdate", "datestamp", "thx", "ifyes", "OtherIntervention", "anleitungandroid", "whyNotIntereseted", "whyNotInstalled", "ifno"]
    survey_relevant_data = survey_df.drop(columns=exclude_columns, errors='ignore')

    # Merge cleaned survey data with the intervention data
    valid_data = valid_data.merge(survey_relevant_data, on='Prolific_ID', how='left')

    # Remove duplicates and sort by Prolific ID
    valid_data = valid_data.drop_duplicates().sort_values(by='Prolific_ID')

    # Export to CSV
    valid_data.to_csv('validData.csv', index=False)
    print("\nValid data including demographic and selected survey information has been saved to 'validData.csv'.\n")

# convert timestamps and calculate personality traits and reactance
def prepare_data(df):
    """
    Prepares data by converting timestamps, calculating personality traits and reactance.

    Parameters:
    - df (pd.DataFrame): Dataframe containing raw data with various metrics.

    Converts 'Timestamp' to datetime to extract the hour and day of the week.
    Computes 'Reactance' as the mean of RSHCI values if columns are present.
    Inverts certain scores for personality traits to align scales, then computes mean scores.
    Results are saved to a new CSV file for further analysis.
    """

    # Convert 'Timestamp' to datetime to extract hour and weekday information
    current_year = pd.to_datetime('today').year
    df['Timestamp'] = pd.to_datetime(df['Timestamp'].apply(lambda x: f"{current_year} {x}"), format='%Y %b %d %H:%M:%S')
    df['Hour'] = df['Timestamp'].dt.hour
    df['Weekday'] = df['Timestamp'].dt.weekday.apply(lambda x: x < 5)  # True if weekday (Monday to Friday)


    # Calculate Reactance as the mean of the RSHCI values if columns are present
    rshci_columns = ['Rshci 1', 'Rshci 2', 'Rshci 3', 'Rshci 4', 'Rshci 5']
    if all(col in df.columns for col in rshci_columns):
        df['Reactance'] = df[rshci_columns].mean(axis=1)


    # Define column names based on provided information
    impulsivity_items = [f"Impulsivity[Imp{i}]" for i in [2, 3, 4, 5, 11, 12, 14, 15]]
    impulsivity_inverted = [f"Impulsivity[Imp{i}]" for i in [1, 6, 7, 8, 9, 10, 13]]
    anxiety_items = [f"Anxiety[Anx{i}]" for i in range(1, 6)]
    self_control_items = [f"SelfControl[SC{i}]" for i in [1, 6, 8, 11]]
    self_control_inverted = [f"SelfControl[SC{i}]" for i in [2, 3, 4, 5, 7, 9, 10, 12, 13]]
    fomo_items = [f"FOMO[Fomo{i}]" for i in range(1, 6)]

    # Invert scores for inverted items
    scale_max_impulsivity = 4
    scale_max_self_control = 5
    for column in impulsivity_inverted:
        df[column] = scale_max_impulsivity + 1 - df[column].astype(float)
    for column in self_control_inverted:
        df[column] = scale_max_self_control + 1 - df[column].astype(float)

    # Compute average scores for personality traits
    df['Impulsivity'] = df[impulsivity_items + impulsivity_inverted].mean(axis=1).round(2)
    df['Anxiety'] = df[anxiety_items].mean(axis=1).round(2)
    df['SelfControl'] = df[self_control_items + self_control_inverted].mean(axis=1).round(2)
    df['FOMO'] = df[fomo_items].mean(axis=1).round(2)

    impulsivity_columns = [f"Impulsivity[Imp{i}]" for i in range(1, 16)]
    self_control_columns = [f"SelfControl[SC{i}]" for i in range(1, 14)]
    fomo_columns = [f"FOMO[Fomo{i}]" for i in range(1, 6)]
    anxiety_columns = [f"Anxiety[Anx{i}]" for i in range(1, 6)]

    # Clean up DataFrame by dropping unnecessary columns
    # Data for Google Sheet
    other_columns = ['Android_ID', 'SelfControl[SCATC]']
    drop_columns = rshci_columns + impulsivity_columns + self_control_columns + fomo_columns + anxiety_columns + other_columns
    df.drop(columns=drop_columns, inplace=True, errors='ignore')

    # Rename columns to match names in Google Sheet
    df.rename(columns={
        'Latency': 'Responsiveness',
        'SelfControl': 'Self-Control',
        'FOMO': 'FoMo',
    }, inplace=True)

    
    # Save the processed data to a new CSV file
    df.to_csv('prepared_data.csv', index=False)
    print("Data has been processed and saved to 'prepared_data.csv'.")


"""
final data
"""
# filters for the data within 7 days
def filter_data_within_7_days(preparedData_df, completed_data):
    """
    Filters the valid data to include only entries within 7 days of the start date.

    Parameters:
    - preparedData_df (DataFrame): DataFrame containing all valid data.
    - completed_data (dict): Dictionary containing {Prolific_ID: (Start_Date, End_Date)} for filtering.

    Returns:
    - None: Saves filtered data to 'filtered_data.csv' and entry counts with payment to 'validEntriesPerID.csv'.
    """

    print("\nFiltering data to retain only entries within 7 days...\n")

    # convert Timestamp
    if 'Timestamp' in preparedData_df.columns:
        preparedData_df['Timestamp'] = pd.to_datetime(preparedData_df['Timestamp'], errors='coerce')
    else:
        print("Error: 'Timestamp' column is missing from the dataset.")
        return None


    # filter for participants in "completed_data" from paymentOverview
    preparedData_df = preparedData_df[preparedData_df['Prolific_ID'].isin(completed_data.keys())]

    filtered_data = preparedData_df[
        preparedData_df.apply(
            lambda row: completed_data[row['Prolific_ID']][0] <= row['Timestamp'] <= completed_data[row['Prolific_ID']][1],
            axis=1
        )
    ]


    # count valid entries per Prolific_ID
    entry_counts = filtered_data['Prolific_ID'].value_counts().reset_index()
    entry_counts.columns = ['Prolific_ID', 'Valid_Entries']

    # add column for Payment
    entry_counts['Payment (£)'] = entry_counts['Valid_Entries'].apply(lambda x: min(x * 0.5, 8.00))


    # find invalid Datapoints
    excluded_data = preparedData_df[
        preparedData_df['Prolific_ID'].isin(completed_data.keys()) &
        (preparedData_df['Timestamp'] > preparedData_df['Prolific_ID'].map(completed_data).apply(lambda x: x[1] if isinstance(x, tuple) else pd.NaT))
    ]

    if not excluded_data.empty:
        print(f"\nExcluded {len(excluded_data)} data points beyond 7 days:")
        print(excluded_data[['Prolific_ID', 'Timestamp']].drop_duplicates())


    # safe filtered data
    filtered_data.to_csv('filtered_data.csv', index=False)
    print("Filtered data has been saved to 'filtered_data.csv'.")

    # safe payment information
    entry_counts.to_csv('validEntriesPerID.csv', index=False)
    print("Valid entry counts per Prolific_ID (with payment) have been saved to 'validEntriesPerID.csv'.")

    print("\nFiltering completed.\n")



################################################################################################################
"""
Function Calls STUDY
"""

# paths to csv's
initial_answers_path = 'initial_answers.csv'
delay_time_path = 'delay_time.csv'
intervention_answers_path = 'intervention_answers.csv'
survey_path = 'results-survey.csv'
survey_extra_path = 'survey_extra.csv'
final_path = 'final.csv'

# read in csv data
initial_answers_df = pd.read_csv(initial_answers_path)
intervention_answers_df = pd.read_csv(intervention_answers_path)
delay_time_df = pd.read_csv(delay_time_path)
surveyresults_df =  pd.read_csv(survey_path)
surveyExtra_df = pd.read_csv(survey_extra_path)
finalQ_df = pd.read_csv(final_path)

# merge survey tables to include unfinished limesurvey data
concatenate_dataframes(surveyresults_df, surveyExtra_df)

# read in merged survey data
survey_path2 = 'results-survey2.csv'
surveyresults2_df =  pd.read_csv(survey_path2)

# rename Columns
initial_df = rename_columns_initialAnswers(initial_answers_df)
intervention_df = rename_columns_interventionAnswers(intervention_answers_df)
delay_df = rename_columns_delay_time(delay_time_df)
survey_df = rename_columns_surveyresults(surveyresults2_df)
final_df = rename_columns_final(finalQ_df)



# remove false attention checks
intervention_df_checked = remove_false_attention_checks(intervention_df)
survey_df_checked = remove_false_attention_checksSurvey(survey_df)

# remove "InfiniteScape" entries
intervention_df_checked = clear_app_name_for_infinite_scape(intervention_df_checked)



# print Payment Overview
completed_data = paymentOverview(initial_df, delay_df, intervention_df_checked, final_df)

# print Registration tracking
#trackRegistration(initial_df, survey_df_checked)

# check for not installed reason 
ids_to_check = [
'5718a9c4dd9ef10013df01f0',
'67cf475074d305ca13b26968',
'67b5e9ef7cb5e64e7e581ecc',
'5c670a430d80fd00014264f9',
'6150d3d8396d00f72a467d0a',
'67c0c81d7a68441d6be8e944',
'66449ec145d0a795084e7b47',
'67efa4929759aa0fd30bebfe',
'673b80fb1398fa8e31faff8b'
]
report_reasons(survey_df, ids_to_check)



# merge all valid data
preprocess_and_export_validData(intervention_df_checked, initial_df, survey_df_checked) # creates validData


# calculate all valid data
validData_path = 'validData.csv'
validData_df = pd.read_csv(validData_path)
prepare_data(validData_df) # creates preparedData

# filter for the valid data within 7 days
prepared_data_path = 'prepared_data.csv'
prepared_data_df = pd.read_csv(prepared_data_path)
filter_data_within_7_days(prepared_data_df, completed_data) # creates filtered_data 


# Plot Data
filtered_data_path = "filtered_data.csv"
filtered_data_df = pd.read_csv(filtered_data_path)


# update demographic information
update_demographic_info_and_save(filtered_data_df, 'filtered_data.csv')

# remove entries to Prolific_IDs with no LimeSurvey data
remove_entries_and_save(filtered_data_df, ['62da5ffdd666bd90db19b4e9'], 'filtered_data.csv')

#################################################################################################################

# find a specific prolific id in df
#find_prolific_id_in_dataset("674ca3ad9c55df2c18692c0f", initial_df)


# prints intervention distribution
def count_interventions(df):
    """
    Counts occurrences of each intervention type and calculates their percentage.
    
    :param intervention_list: List of intervention types (e.g., ["Pop-Up", "Vibration", "SpotOverlay"])
    :return: Dictionary with counts and percentages
    """
    # Häufigkeiten berechnen
    intervention_counts = df['Intervention_Type'].value_counts()
    
    # Prozentuale Verteilung berechnen
    intervention_percentages = (intervention_counts / intervention_counts.sum()) * 100
    
    # Ergebnisse ausgeben
    print("Häufigkeiten der Interventionen:")
    print(intervention_counts)
    print("\nProzentuale Verteilung der Interventionen:")
    print(intervention_percentages.round(2))
    
    return intervention_counts, intervention_percentages
    
count_interventions(prepared_data_df)


